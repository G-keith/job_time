package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.*;
import com.job.entity.vo.JobVo;
import com.job.entity.vo.UserJobVo;
import com.job.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JobService {

    private final JobMapper jobMapper;

    private final JobStepMapper jobStepMapper;

    private final SysBondMapper sysBondMapper;

    private final UserMoneyMapper userMoneyMapper;

    private final UserJobMapper userJobMapper;

    public JobService(JobMapper jobMapper, JobStepMapper jobStepMapper, SysBondMapper sysBondMapper, UserMoneyMapper userMoneyMapper, UserJobMapper userJobMapper) {
        this.jobMapper = jobMapper;
        this.jobStepMapper = jobStepMapper;
        this.sysBondMapper = sysBondMapper;
        this.userMoneyMapper = userMoneyMapper;
        this.userJobMapper = userJobMapper;
    }

    /**
     * 查询所有待审核任务
     *
     * @return
     */
    public ServerResponse findAll(String jobTitle, String jobSource, Integer pageNo, Integer pageSize) {
        Page<JobVo> page = PageHelper.startPage(pageNo, pageSize);
        jobMapper.findAll(jobTitle, jobSource);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 根据主键id查询步骤详情
     *
     * @param jobId 主键id
     * @return
     */
    public ServerResponse findAllStep(Integer jobId) {
        return ServerResponse.createBySuccess(jobMapper.findAllStep(jobId));
    }

    /**
     * 插入任务审核结果
     *
     * @param jobId       任务id
     * @param auditStatus 审核结果
     * @return
     */
    public ServerResponse updateJob(Integer jobId, Integer auditStatus, String refuseReason, Integer label, Integer isRecommend) {
        int result = jobMapper.updateJob(jobId, auditStatus, refuseReason, label, isRecommend);
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询用户足迹
     *
     * @param userId
     * @return
     */
    public ServerResponse findFootprint(Integer userId, Integer pageNo, Integer pageSize) {
        Page<JobVo> page = PageHelper.startPage(pageNo, pageSize);
        jobMapper.findFootprint(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询用户发布的任务
     *
     * @param userId      用户id
     * @param auditStatus 审核状态
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findRelease(Integer userId, Integer auditStatus, Integer pageNo, Integer pageSize) {
        Page<JobVo> page = PageHelper.startPage(pageNo, pageSize);
        jobMapper.findRelease(userId, auditStatus);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询需要审核任务列表
     *
     * @param userId   用户id
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findWillAudit(Integer userId, Integer pageNo, Integer pageSize) {
        Page<JobVo> page = PageHelper.startPage(pageNo, pageSize);
        jobMapper.findWillAudit(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询任务审核列表
     *
     * @param jobId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findUserJob(Integer jobId, Integer pageNo, Integer pageSize) {
        Page<UserJobVo> page = PageHelper.startPage(pageNo, pageSize);
        jobMapper.findUserJob(jobId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询验证图
     *
     * @param taskId
     * @return
     */
    public ServerResponse findCheckPicture(Integer taskId) {
        return ServerResponse.createBySuccess(jobMapper.findCheckPicture(taskId));
    }

    /**
     * 插入用户发布的任务
     *
     * @param job
     * @return
     */
    public ServerResponse insertJob(Job job) {
        UserMoney userMoney = userMoneyMapper.selectById(job.getUserId());
        int res = jobMapper.findUserIsMember(job.getUserId());
        ServiceFee serviceFee = jobMapper.findFee();
        if (res > 0) {
            //代表是会员
            job.setReleasePrice(getPrice(job.getJobPrice(), serviceFee.getMemberRate()));
        } else {
            job.setReleasePrice(getPrice(job.getJobPrice(), serviceFee.getCommonRate()));
        }
        job.setReleaseTime(new Date());
        // 更新用户账户余额，钱打入系统账户
        BigDecimal totalPrice = getTotalPrice(job.getJobPrice(), job.getJobNum());
        BigDecimal surplus = surplusPrice(userMoney.getRepaidBalance(), totalPrice);
        if (surplus.doubleValue() < 0) {
            return ServerResponse.createByErrorCodeMessage(2, "账户余额不足");
        } else {
            userMoney.setRepaidBalance(surplus);
            //减去用户账户钱，添加到系统账户中去
            userMoneyMapper.updateMoney(userMoney);
            //系统账户加上任务钱
            userMoneyMapper.updateAdmin(userMoneyMapper.money().add(totalPrice));
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(job.getUserId());
            userMoneyDetails.setType(3);
            userMoneyDetails.setExplain("发布任务");
            userMoneyDetails.setMoney(job.getReleasePrice().negate().setScale(2, BigDecimal.ROUND_HALF_UP));
            userMoneyDetails.setTradeTime(new Date());
            userMoneyMapper.insertMoneyDetails(userMoneyDetails);
        }
        //1.插入任务表；
        int result = jobMapper.insertSelective(job);
        if (result > 0) {
            //插入任务和类型关系表
            jobMapper.insertJobType(job.getJobId(), job.getTypeId());
            //2.插入任务步骤
            job.getStepList().forEach(e -> {
                e.setJobId(job.getJobId());
                jobStepMapper.insertSelective(e);
            });
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 插入用户审核结果
     *
     * @param taskId
     * @param status
     * @return
     */
    public ServerResponse updateUserJob(Integer taskId, Integer status, String refuseReason) {
        UserJob userJob = userJobMapper.findById(taskId);
        Job job = jobMapper.selectJob(userJob.getJobId());
        int result = userJobMapper.updateUserJob(taskId, status, refuseReason);
        if (result > 0) {
            //审核通过，钱进入用户账户
            if (status == 4) {
                //扣除系统账户余额，钱打入用户账户中去
                UserMoney userMoney = userMoneyMapper.selectById(userJob.getUserId());
                //系统账户钱减去任务发布钱
                userMoneyMapper.updateAdmin(surplusPrice(userMoneyMapper.money(), job.getReleasePrice()));
                //用户账户加上任务钱
                userMoney.setBalance(userMoney.getRepaidBalance().add(job.getReleasePrice()));
                userMoneyMapper.updateMoney(userMoney);
                //插入明细
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setUserId(userJob.getUserId());
                userMoneyDetails.setType(2);
                userMoneyDetails.setExplain("任务奖励");
                userMoneyDetails.setMoney(job.getReleasePrice());
                userMoneyDetails.setTradeTime(new Date());
                userMoneyMapper.insertMoneyDetails(userMoneyDetails);
                //更新任务完成数量
                job.setCommitNum(job.getCommitNum() - 1);
                job.setFinishNum(job.getFinishNum() + 1);
                jobMapper.updateByPrimaryKeySelective(job);
            }
            //审核拒绝
            if (status == 5) {
                //更新任务剩余数量
                job.setCommitNum(job.getCommitNum() - 1);
                job.setJobNum(job.getJobNum() + 1);
                if (job.getJobStatus() == 2) {
                    //任务已经结束，归还任务的钱
                    //发布任务用户账户加上钱
                    UserMoney userMoney = userMoneyMapper.selectById(job.getUserId());
                    userMoney.setRepaidBalance(userMoney.getRepaidBalance().add(job.getJobPrice()));
                    userMoneyMapper.updateMoney(userMoney);
                    //插入明细
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setUserId(userJob.getUserId());
                    userMoneyDetails.setType(3);
                    userMoneyDetails.setExplain("任务退还");
                    userMoneyDetails.setMoney(job.getJobPrice());
                    userMoneyDetails.setTradeTime(new Date());
                    userMoneyMapper.insertMoneyDetails(userMoneyDetails);
                    //系统账户钱减去任务钱
                    userMoneyMapper.updateAdmin(surplusPrice(userMoneyMapper.money(), job.getJobPrice()));
                }
                jobMapper.updateByPrimaryKeySelective(job);
            }
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 暂停或者接受任务
     *
     * @param jobId
     * @param type
     * @return
     */
    public ServerResponse suspendOrEnd(Integer jobId, Integer type) {
        Job job = jobMapper.selectJob(jobId);
        UserMoney userMoney = userMoneyMapper.selectById(job.getUserId());
        if (type == 2) {
            //任务结束，剩余钱返还
            Integer surplusNum = job.getSurplusNum();
            if (surplusNum > 0) {
                //任务没做完剩余金钱
                BigDecimal surplusMoney = getTotalPrice(job.getJobPrice(), surplusNum);
                userMoney.setRepaidBalance(addPrice(userMoney.getRepaidBalance(), surplusMoney));
                //更新用户账户余额
                userMoneyMapper.updateMoney(userMoney);
                //插入明细
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setUserId(job.getUserId());
                userMoneyDetails.setType(3);
                userMoneyDetails.setExplain("任务退还");
                userMoneyDetails.setMoney(surplusMoney);
                userMoneyDetails.setTradeTime(new Date());
                userMoneyMapper.insertMoneyDetails(userMoneyDetails);
                //系统账户减少
                userMoneyMapper.updateAdmin(surplusPrice(userMoneyMapper.money(), surplusMoney));
            }
        }
        job.setJobStatus(type);
        int result = jobMapper.updateByPrimaryKeySelective(job);
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 获取发布价格
     *
     * @param jobPrice
     * @param rate
     * @return
     */
    private BigDecimal getPrice(BigDecimal jobPrice, BigDecimal rate) {
        BigDecimal feePrice = jobPrice.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
        return jobPrice.subtract(feePrice).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取任务总额
     *
     * @param jobPrice
     * @param jobNum
     * @return
     */
    private BigDecimal getTotalPrice(BigDecimal jobPrice, Integer jobNum) {
        return jobPrice.multiply(new BigDecimal(jobNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取任务总额
     *
     * @param b1
     * @param b2
     * @return
     */
    private BigDecimal addPrice(BigDecimal b1, BigDecimal b2) {
        return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取剩余金钱
     *
     * @param repaidBalance
     * @param totalPrice
     * @return
     */
    private BigDecimal surplusPrice(BigDecimal repaidBalance, BigDecimal totalPrice) {
        return repaidBalance.subtract(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
