package com.job.task;

import com.job.entity.*;
import com.job.mapper.JobMapper;
import com.job.mapper.TimerTaskMapper;
import com.job.mapper.UserMoneyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时器
 *
 * @author keith
 * @version 1.0
 * @date 2019/11/20
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class TimerTask {

    private final TimerTaskMapper timerTaskMapper;

    private final UserMoneyMapper userMoneyMapper;

    private final JobMapper jobMapper;

    public TimerTask(TimerTaskMapper timerTaskMapper, UserMoneyMapper userMoneyMapper, JobMapper jobMapper) {
        this.timerTaskMapper = timerTaskMapper;
        this.userMoneyMapper = userMoneyMapper;
        this.jobMapper = jobMapper;
    }

    //@Scheduled(cron = "0  */1 * * * ?")
    public void cancelMember() {
        //取消会员
        member();
        //任务过期
        timerTaskMapper.jobExpire();
        //自动审核任务通过
        //jobCommit();
        reportCommit();
    }


    public void member() {
        List<UserInfo> userInfoList = timerTaskMapper.findMember();
        userInfoList.forEach(e -> {
            //当前是年会员，判断年有没有过期
            if (e.getIsMember() == 5 && e.getYearMemberTime().getTime() < System.currentTimeMillis()) {
                e.setIsMember(4);
            }
            //当前是季会员，判断年有没有过期
            if (e.getIsMember() == 4 && e.getSeasonMemberTime().getTime() < System.currentTimeMillis()) {
                e.setIsMember(3);
            }
            //当前是月会员，判断年有没有过期
            if (e.getIsMember() == 3 && e.getMonthMemberTime().getTime() < System.currentTimeMillis()) {
                e.setIsMember(2);
            }
            //当前是周会员，判断年有没有过期
            if (e.getIsMember() == 2 && e.getWeekMemberTime().getTime() < System.currentTimeMillis()) {
                System.out.println("周会员");
                //过期改为非会员
                e.setIsMember(1);
            }
        });
        timerTaskMapper.updateMember(userInfoList);
    }

    //举报信息自动提交
    public void reportCommit(){
        List<UserReport> userReports=new ArrayList<>();
        List<UserReport> userReportList=timerTaskMapper.findNotEnd();
        userReportList.forEach(e->{
            if(e.getReportStatus()==1){
                if(System.currentTimeMillis()-e.getReportTime().getTime()>86400000){
                    userReports.add(e);
                }
            }
            if(e.getReportStatus()==2){
                if(System.currentTimeMillis()-e.getReplyTime().getTime()>86400000){
                    userReports.add(e);
                }
            }
        });
    }

    /**
     * 自动审核任务通过
     */
    public void jobCommit() {
        List<UserJob> userJobList = timerTaskMapper.jobCommit(2);
        userJobList.forEach(e -> {
            //扣除系统账户余额，钱打入用户账户中去
            UserMoney userMoney = userMoneyMapper.selectById(e.getUserId());
            //系统账户钱减去任务发布钱
            userMoneyMapper.updateAdmin(surplusPrice(userMoneyMapper.money(), e.getReleasePrice()));
            //用户账户加上任务钱
            userMoney.setBalance(userMoney.getBalance().add(e.getReleasePrice()));
            userMoneyMapper.updateMoney(userMoney);
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setType(2);
            userMoneyDetails.setUserId(e.getUserId());
            userMoneyDetails.setMoney(e.getReleasePrice());
            userMoneyDetails.setIntroduce("任务奖励");
            userMoneyDetails.setTradeTime(new Date());
            userMoneyMapper.insertMoneyDetails(userMoneyDetails);
            //更新任务完成数量
            Job job = jobMapper.selectJob(e.getJobId());
            job.setCommitNum(job.getCommitNum() - 1);
            job.setFinishNum(job.getFinishNum() + 1);
            jobMapper.updateByPrimaryKeySelective(job);
        });
        if (userJobList.size() > 0) {
            timerTaskMapper.updateJobCommit(userJobList);
        }
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
