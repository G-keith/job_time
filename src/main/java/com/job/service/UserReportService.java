package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.*;
import com.job.entity.vo.UserReportVo;
import com.job.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/4
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserReportService {

    private final UserReportMapper userReportMapper;

    private final UserJobMapper userJobMapper;

    private final JobMapper jobMapper;

    private final UserMoneyMapper userMoneyMapper;

    private final UserInfoMapper userInfoMapper;

    private final HomePageMapper homePageMapper;

    public UserReportService(UserReportMapper userReportMapper, UserJobMapper userJobMapper, JobMapper jobMapper, UserMoneyMapper userMoneyMapper, UserInfoMapper userInfoMapper, HomePageMapper homePageMapper) {
        this.userReportMapper = userReportMapper;
        this.userJobMapper = userJobMapper;
        this.jobMapper = jobMapper;
        this.userMoneyMapper = userMoneyMapper;
        this.userInfoMapper = userInfoMapper;
        this.homePageMapper = homePageMapper;
    }

    /**
     * 查询用户举报的未结束的列表
     * @param pageNo
     * @param pageSize
     * @param reportStatus
     * @param userId
     * @return
     */
    public ServerResponse findAll(Integer pageNo,Integer pageSize,Integer reportStatus,Integer userId){
        Page<UserReportVo> page = PageHelper.startPage(pageNo, pageSize);
        userReportMapper.findAll(reportStatus, userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询悬赏主被举报的未列表
     * @param pageNo
     * @param pageSize
     * @param reportStatus
     * @param userId
     * @return
     */
    public ServerResponse findRewardAll(Integer pageNo,Integer pageSize,Integer reportStatus,Integer userId){
        Page<UserReportVo> page = PageHelper.startPage(pageNo, pageSize);
        userReportMapper.findRewardAll(reportStatus, userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询用户举报已经审核的列表
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    public ServerResponse findUserAudit(Integer pageNo,Integer pageSize,Integer userId){
        Page<UserReportVo> page = PageHelper.startPage(pageNo, pageSize);
        userReportMapper.findUserAudit(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询悬赏主被举报已经审核的列表
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    public ServerResponse findRewardAudit(Integer pageNo,Integer pageSize,Integer userId){
        Page<UserReportVo> page = PageHelper.startPage(pageNo, pageSize);
        userReportMapper.findRewardAudit(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 后台查询已经审核的列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findAudit(Integer pageNo,Integer pageSize){
        Page<UserReportVo> page = PageHelper.startPage(pageNo, pageSize);
        userReportMapper.findAudit();
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 后台查询待审核的列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findWillAudit(Integer pageNo,Integer pageSize){
        Page<UserReportVo> page = PageHelper.startPage(pageNo, pageSize);
        userReportMapper.findWillAudit();
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 插入举报信息
     * @param report
     * @return
     */
    public ServerResponse insertReport(UserReport report){
        UserInfo userInfo=userInfoMapper.findByUserId(report.getUserId());
        if(userInfo.getStatus()==1){
            return ServerResponse.createByErrorCodeMessage(2, "用户为黑名单，不可进行操作");
        }
        UserJob userJob=userJobMapper.findById(report.getTaskId());
        Job job=jobMapper.selectJob(userJob.getJobId());
        report.setJobId(userJob.getJobId());
        report.setRewardId(job.getUserId());
        int result=userReportMapper.insertSelective(report);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 更新举报信息
     * @param report
     * @return
     */
    public ServerResponse updateReport(UserReport report){
        int result=userReportMapper.updateByPrimaryKeySelective(report);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    public ServerResponse updateAudit(UserReport report){
        int result=userReportMapper.updateByPrimaryKeySelective(report);
        if(result>0){
        report=userReportMapper.selectByPrimaryKey(report.getReportId());
        Job job=jobMapper.selectJob(report.getJobId());
        UserInfo userInfo=userInfoMapper.findByUserId(report.getUserId());
        //审核通过
        if(report.getReportStatus()==4){
            //任务钱打入用户账户中去
            UserMoney userMoney = userMoneyMapper.selectById(report.getUserId());
            userMoney.setBalance(addPrice(userMoney.getBalance(), job.getReleasePrice()));
            userMoneyMapper.updateMoney(userMoney);
            //扣除发布者账户余额
            UserMoney rewardMoney = userMoneyMapper.selectById(report.getRewardId());
            rewardMoney.setRepaidBalance(surplusPrice(rewardMoney.getRepaidBalance(), job.getReleasePrice()));
            userMoneyMapper.updateMoney(rewardMoney);
            //系统账户加上收费
            BigDecimal money=surplusPrice(job.getJobPrice(),job.getReleasePrice());
            userMoneyMapper.updateAdmin(addPrice(userMoneyMapper.money(),money));
            //插入发布者和用户账户明细
            insertDetails(report.getUserId(),job.getReleasePrice(),"举报-审核通过",2);
            insertDetails(report.getUserId(),job.getJobPrice().negate().setScale(2, BigDecimal.ROUND_HALF_UP),"被举报-审核通过",2);
            //举报来源是用户审核拒绝，所以，任务数量减1，完成数量加1，更新任务完成数量
            job.setCommitNum(job.getJobNum() - 1);
            job.setFinishNum(job.getFinishNum() + 1);
            jobMapper.updateByPrimaryKeySelective(job);
            //计算推广人奖励
            insertInvite(userInfo.getUpUID(),job.getReleasePrice());
        }
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    public void insertInvite(String uid,BigDecimal jobReleasePrice){
        // 查询用户的上级
        if (uid != null) {
            //一级推广赏金比例金钱
            SignInMoney signInMoney = homePageMapper.selectSignInMoney();
            //根据推荐码去查询邀请人信息
            Integer userId = userInfoMapper.findByUId(uid).getUserId();
            userMoneyMapper.updateMoney(inviteReward(userId,signInMoney.getOneInvite(),jobReleasePrice));
            //二级推广奖励
            UserInfo twoUserInfo=userInfoMapper.findByUserId(userId);
            if(twoUserInfo.getUpUID()!=null){
                Integer twoUserId = userInfoMapper.findByUId(twoUserInfo.getUpUID()).getUserId();
                userMoneyMapper.updateMoney(inviteReward(twoUserId,signInMoney.getTwoInvite(),jobReleasePrice));
            }
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
        insertDetails(userId,inviteMoney,"邀请奖励",4);
        return userMoney;
    }

    private void insertDetails(Integer userId,BigDecimal money,String introduce,Integer type){
        //插入明细
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setType(type);
        userMoneyDetails.setIntroduce(introduce);
        userMoneyDetails.setMoney(money);
        userMoneyDetails.setTradeTime(new Date());
        userMoneyMapper.insertMoneyDetails(userMoneyDetails);
    }

    /**
     * 查询任务详情
     * @param taskId
     * @return
     */
    public ServerResponse selectJobDetails(Integer taskId){
        return ServerResponse.createBySuccess(userReportMapper.selectJobDetails(taskId));
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
