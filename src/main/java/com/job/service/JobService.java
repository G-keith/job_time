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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    private final UserInfoMapper userInfoMapper;

    private final HomePageMapper homePageMapper;

    public JobService(JobMapper jobMapper, JobStepMapper jobStepMapper, SysBondMapper sysBondMapper, UserMoneyMapper userMoneyMapper, UserJobMapper userJobMapper, UserInfoMapper userInfoMapper, HomePageMapper homePageMapper) {
        this.jobMapper = jobMapper;
        this.jobStepMapper = jobStepMapper;
        this.sysBondMapper = sysBondMapper;
        this.userMoneyMapper = userMoneyMapper;
        this.userJobMapper = userJobMapper;
        this.userInfoMapper = userInfoMapper;
        this.homePageMapper = homePageMapper;
    }

    /**
     * 查询所有待审核任务
     *
     * @return
     */
    public ServerResponse findAll(String jobTitle, String jobSource,Integer typeId, Integer pageNo, Integer pageSize) {
        Page<JobVo> page = PageHelper.startPage(pageNo, pageSize);
        jobMapper.findAll(jobTitle, jobSource,typeId);
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
    public ServerResponse updateJob(Integer jobId, Integer auditStatus, String refuseReason, Integer isRecommend) {
        int result = jobMapper.updateJob(jobId, auditStatus, refuseReason, isRecommend);
        if (result > 0) {
            if(auditStatus==4){
                Job job=jobMapper.selectJob(jobId);
                UserMoney userMoney=userMoneyMapper.selectById(job.getUserId());
                //审核拒绝，归还用户金钱
                BigDecimal surplusMoney = getTotalPrice(job.getJobPrice(), job.getJobNum());
                userMoney.setRepaidBalance(addPrice(userMoney.getRepaidBalance(), surplusMoney));
                //更新用户账户余额
                userMoneyMapper.updateMoney(userMoney);
                //系统账户减少
                userMoneyMapper.updateAdmin(surplusPrice(userMoneyMapper.money(), surplusMoney));
            }
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
     * 查询用户已结束的任务
     *
     * @param userId      用户id
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findEndRelease(Integer userId ,Integer pageNo, Integer pageSize) {
        Page<JobVo> page = PageHelper.startPage(pageNo, pageSize);
        jobMapper.findEndRelease(userId);
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
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findUserJob(Integer status, Integer pageNo, Integer pageSize,Integer userId) {
        Page<UserJobVo> page = PageHelper.startPage(pageNo, pageSize);
        List<UserJobVo> userJobVoList= jobMapper.findUserJob(status,userId);
        userJobVoList.forEach(e-> e.setImgList(jobMapper.findCheckPicture(e.getTaskId())));
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
        UserInfo userInfo = userInfoMapper.findByUserId(job.getUserId());
        if(userInfo.getStatus()==1){
            return ServerResponse.createByErrorCodeMessage(2, "用户为黑名单，不可进行操作");
        }
        ServiceFee serviceFee = jobMapper.findFee();
        if (userMoney.getJobNum() == 0 && userInfo.getIsMember() < 4) {
            return ServerResponse.createByErrorMessage("发布任务达到上限");
        }
        if (userInfo.getIsMember() == 2 && userInfo.getWeekMemberTime().getTime() > System.currentTimeMillis()) {
            //周会员
            job.setReleasePrice(getPrice(job.getJobPrice(), serviceFee.getWeekRate()));
        } else if (userInfo.getIsMember() == 3 && userInfo.getMonthMemberTime().getTime() > System.currentTimeMillis()) {
            //月会员
            job.setReleasePrice(getPrice(job.getJobPrice(), serviceFee.getMonthRate()));
        } else if (userInfo.getIsMember() == 4 && userInfo.getSeasonMemberTime().getTime() > System.currentTimeMillis()) {
            //季会员
            job.setReleasePrice(getPrice(job.getJobPrice(), serviceFee.getSeasonRate()));
        } else if (userInfo.getIsMember() == 5 && userInfo.getYearMemberTime().getTime() > System.currentTimeMillis()) {
            //年会员
            job.setReleasePrice(getPrice(job.getJobPrice(), serviceFee.getYearRate()));
        } else {
            //不是会员
            job.setReleasePrice(getPrice(job.getJobPrice(), serviceFee.getCommonRate()));
        }
        job.setReleaseTime(new Date());
        job.setRefreshTime(new Date());
        // 更新用户账户余额，钱打入系统账户
        BigDecimal totalPrice = getTotalPrice(job.getJobPrice(), job.getJobNum());
        BigDecimal surplus = surplusPrice(userMoney.getRepaidBalance(), totalPrice);
        if (surplus.doubleValue() < 0) {
            return ServerResponse.createByErrorCodeMessage(2, "账户余额不足");
        } else {
            userMoney.setRepaidBalance(surplus);
            userMoney.setJobNum(userMoney.getJobNum() - 1);
            //减去用户账户钱，添加到系统账户中去
            userMoneyMapper.updateMoney(userMoney);
            //系统账户加上任务钱
            userMoneyMapper.updateAdmin(addPrice(userMoneyMapper.money(), totalPrice));
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(job.getUserId());
            userMoneyDetails.setType(3);
            userMoneyDetails.setIntroduce("发布任务");
            userMoneyDetails.setMoney(job.getReleasePrice().negate().setScale(2, BigDecimal.ROUND_HALF_UP));
            userMoneyDetails.setTradeTime(new Date());
            userMoneyMapper.insertMoneyDetails(userMoneyDetails);
        }
        //1.插入任务表；
        int result = jobMapper.insertSelective(job);
        if (result > 0) {
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
        UserInfo userInfo = userInfoMapper.findByUserId(userJob.getUserId());
        if(userInfo.getStatus()==1){
            return ServerResponse.createByErrorCodeMessage(2, "用户为黑名单，不可进行操作");
        }
        int result = userJobMapper.updateUserJob(taskId, status, refuseReason);
        if (result > 0) {
            //审核通过，钱进入用户账户
            if (status == 4) {
                //扣除系统账户余额，钱打入用户账户中去
                UserMoney userMoney = userMoneyMapper.selectById(userJob.getUserId());
                //系统账户钱减去任务发布钱
                userMoneyMapper.updateAdmin(surplusPrice(userMoneyMapper.money(), job.getReleasePrice()));
                //用户账户加上任务钱
                userMoney.setBalance(addPrice(userMoney.getBalance(), job.getReleasePrice()));
                userMoneyMapper.updateMoney(userMoney);
                //插入明细
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setUserId(userJob.getUserId());
                userMoneyDetails.setType(2);
                userMoneyDetails.setIntroduce("任务奖励");
                userMoneyDetails.setMoney(job.getReleasePrice());
                userMoneyDetails.setTradeTime(new Date());
                userMoneyMapper.insertMoneyDetails(userMoneyDetails);
                //更新任务完成数量
                job.setCommitNum(job.getCommitNum() - 1);
                job.setFinishNum(job.getFinishNum() + 1);
                if(job.getJobNum().equals(job.getFinishNum())){
                    job.setJobStatus(2);
                }
                jobMapper.updateByPrimaryKeySelective(job);
                // 查询用户的上级
                if (userInfo.getUpUID() != null) {
                    //一级推广赏金比例金钱
                    SignInMoney signInMoney = homePageMapper.selectSignInMoney();
                    //根据推荐码去查询邀请人信息
                    Integer userId = userInfoMapper.findByUId(userInfo.getUpUID()).getUserId();
                    userMoneyMapper.updateMoney(inviteReward(userId,signInMoney.getOneInvite(),job.getReleasePrice()));
                    //二级推广奖励
                    UserInfo twoUserInfo=userInfoMapper.findByUserId(userId);
                    if(twoUserInfo.getUpUID()!=null){
                        Integer twoUserId = userInfoMapper.findByUId(twoUserInfo.getUpUID()).getUserId();
                        userMoneyMapper.updateMoney(inviteReward(twoUserId,signInMoney.getTwoInvite(),job.getReleasePrice()));
                    }
                }
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
                    userMoney.setRepaidBalance(addPrice(userMoney.getRepaidBalance(), job.getJobPrice()));
                    userMoneyMapper.updateMoney(userMoney);
                    //插入明细
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setUserId(userJob.getUserId());
                    userMoneyDetails.setType(3);
                    userMoneyDetails.setIntroduce("任务退还");
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
     * 刷新任务
     * @param jobId
     * @return
     */
    public ServerResponse refreshJob(Integer jobId){
        Job job=jobMapper.selectJob(jobId);
        UserInfo userInfo=userInfoMapper.findByUserId(job.getUserId());
        if(userInfo.getStatus()==1){
            return ServerResponse.createByErrorCodeMessage(2, "用户为黑名单，不可进行操作");
        }
        UserMoney userMoney = userMoneyMapper.selectById(job.getUserId());
        BigDecimal surplusPrice;
        if(userInfo.getIsMember()==1){
            //不是会员
            surplusPrice=surplusPrice(userMoney.getRepaidBalance(),new BigDecimal(10));
        }else{
            //是会员
            surplusPrice=surplusPrice(userMoney.getRepaidBalance(),new BigDecimal(5));
        }
        if(surplusPrice.doubleValue()<0){
            return ServerResponse.createByErrorMessage("账户余额不足");
        }else{
            job.setRefreshTime(new Date());
            jobMapper.updateByPrimaryKeySelective(job);
            userMoney.setRepaidBalance(surplusPrice);
            userMoneyMapper.updateMoney(userMoney);
            return ServerResponse.createBySuccess();
        }
    }
    /**
     * 获取用户账户信息
     * @param userId
     * @param rate
     * @param releasePrice
     * @return
     */
    private UserMoney inviteReward(Integer userId,BigDecimal rate,BigDecimal releasePrice){
        BigDecimal inviteMoney=releasePrice.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
        UserMoney userMoney = userMoneyMapper.selectById(userId);
        userMoney.setBonus(addPrice(userMoney.getBonus(), inviteMoney));
        //插入用户明细
        insertDetails(userId,inviteMoney);
        return userMoney;
    }
    private void insertDetails(Integer userId,BigDecimal money){
        //插入明细
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setType(4);
        userMoneyDetails.setIntroduce("邀请奖励");
        userMoneyDetails.setMoney(money);
        userMoneyDetails.setTradeTime(new Date());
        userMoneyMapper.insertMoneyDetails(userMoneyDetails);
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
        UserInfo userInfo=userInfoMapper.findByUserId(job.getUserId());
        if(userInfo.getStatus()==1){
            return ServerResponse.createByErrorCodeMessage(2, "用户为黑名单，不可进行操作");
        }
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
                userMoneyDetails.setIntroduce("任务退还");
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
     * 乘
     *
     * @param jobPrice
     * @param jobNum
     * @return
     */
    private BigDecimal getTotalPrice(BigDecimal jobPrice, Integer jobNum) {
        return jobPrice.multiply(new BigDecimal(jobNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 加
     *
     * @param b1
     * @param b2
     * @return
     */
    private BigDecimal addPrice(BigDecimal b1, BigDecimal b2) {
        return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 减
     *
     * @param repaidBalance
     * @param totalPrice
     * @return
     */
    private BigDecimal surplusPrice(BigDecimal repaidBalance, BigDecimal totalPrice) {
        return repaidBalance.subtract(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
