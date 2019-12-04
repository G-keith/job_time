package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserMoney;
import com.job.entity.UserMoneyDetails;
import com.job.entity.vo.JobDto;
import com.job.entity.vo.JobListVo;
import com.job.entity.vo.JobVo;
import com.job.mapper.HomePageMapper;
import com.job.mapper.UserMoneyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HomePageService {

    @Autowired
    private HomePageMapper homePageMapper;

    @Autowired
    private UserMoneyMapper userMoneyMapper;

    /**
     * 查询所有首页轮播图
     *
     * @return 首页轮播图
     */
    public ServerResponse findAll() {
        return ServerResponse.createBySuccess(homePageMapper.findAll());
    }

    /**
     * 查询推荐任务列表信息
     *
     * @param pageNo   第几页
     * @param pageSize 每页几条
     * @return 任务推荐任务列表信息
     */
    public ServerResponse<PageVO<JobListVo>> findRecommend(Integer pageNo, Integer pageSize) {
        Page<JobListVo> page = PageHelper.startPage(pageNo, pageSize);
        homePageMapper.findRecommend();
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 用户当天是否签到过
     *
     * @param userId 用户id
     * @return 1没有，2有
     */
    public ServerResponse isSignIn(Integer userId) {
        int result = homePageMapper.isSignIn(userId);
        if (result == 1) {
            return ServerResponse.createBySuccess("已经签到过", 1);
        } else {
            return ServerResponse.createBySuccess("没有签到过", 2);
        }
    }

    /**
     * 插入用户签到信息
     *
     * @param userId 用户id
     * @return 0成功，1失败
     */
    public ServerResponse insertSignIn(Integer userId) {
        System.out.println(userId+"====");
        int result = homePageMapper.insertSignIn(userId);
        if (result > 0) {
            BigDecimal balance=homePageMapper.selectSignInMoney().getMoney();
            //增加账户余额
            UserMoney userMoney = homePageMapper.selectByUserId(userId);
            userMoney.setBalance(userMoney.getBalance().add(balance).setScale(2,BigDecimal.ROUND_HALF_UP));
            userMoneyMapper.updateMoney(userMoney);
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setType(2);
            userMoneyDetails.setIntroduce("每日签到");
            userMoneyDetails.setMoney(balance);
            userMoneyDetails.setTradeTime(new Date());
            userMoneyMapper.insertMoneyDetails(userMoneyDetails);
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }
}
