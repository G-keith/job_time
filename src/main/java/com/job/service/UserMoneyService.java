package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.DateUtils;
import com.job.entity.UserInfo;
import com.job.entity.UserMoney;
import com.job.entity.UserMoneyDetails;
import com.job.entity.UserOrder;
import com.job.mapper.UserInfoMapper;
import com.job.mapper.UserMoneyMapper;
import com.job.mapper.UserOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMoneyService {

    private final UserMoneyMapper userMoneyMapper;

    private final UserOrderMapper userOrderMapper;

    private final UserInfoMapper userInfoMapper;

    private final UserChatService userChatService;


    public UserMoneyService(UserMoneyMapper userMoneyMapper, UserOrderMapper userOrderMapper, UserInfoMapper userInfoMapper, UserChatService userChatService) {
        this.userMoneyMapper = userMoneyMapper;
        this.userOrderMapper = userOrderMapper;
        this.userInfoMapper = userInfoMapper;
        this.userChatService = userChatService;
    }

    /**
     * 查询用户账户信息
     *
     * @param userId 用户id
     * @return
     */
    public ServerResponse<UserMoney> findMoney(Integer userId) {
        return ServerResponse.createBySuccess(userMoneyMapper.selectById(userId));
    }

    /**
     * 支付成功后逻辑
     *
     * @param userOrder
     * @return
     */
    public void paySuccess(UserOrder userOrder) {
        //会员充值
        UserInfo userInfo = userInfoMapper.findByUserId(userOrder.getUserId());
        UserMoney userMoney = userMoneyMapper.selectById(userOrder.getUserId());
        userChatService.insetChatRecord(32, userOrder.getUserId(), "充值成功，时间为"+DateUtils.getDate(new Date()));
        if (userOrder.getOrderType() == 1) {
            if (userOrder.getOrderMold() == 1) {
                //周充值
                if (userInfo.getWeekMemberTime() != null && userInfo.getWeekMemberTime().getTime() > System.currentTimeMillis()) {
                    //会员没有到期，加上续费会员时间
                    userInfo.setWeekMemberTime(DateUtils.getDate_add(userInfo.getYearMemberTime(), 7, 1));
                } else {
                    //会员到期
                    userInfo.setWeekMemberTime(DateUtils.getDate_add(new Date(), 7, 1));
                }
                if (userInfo.getIsMember() < 2) {
                    userInfo.setIsMember(2);
                }
                userMoney.setJobNum(userMoney.getJobNum() + 5);
                userMoney.setRefreshNum(userMoney.getRefreshNum() + 1);
            }
            if (userOrder.getOrderMold() == 2) {
                //月充值
                if (userInfo.getMonthMemberTime() != null && userInfo.getMonthMemberTime().getTime() > System.currentTimeMillis()) {
                    //会员没有到期，加上续费会员时间
                    userInfo.setMonthMemberTime(DateUtils.getDate_add(userInfo.getYearMemberTime(), 1, 2));
                } else {
                    //会员到期
                    userInfo.setMonthMemberTime(DateUtils.getDate_add(new Date(), 1, 2));
                }
                if (userInfo.getIsMember() < 3) {
                    userInfo.setIsMember(3);
                }
                userMoney.setJobNum(userMoney.getJobNum() + 8);
                userMoney.setRefreshNum(userMoney.getRefreshNum() + 5);
            }
            if (userOrder.getOrderMold() == 3) {
                //季充值
                if (userInfo.getSeasonMemberTime() != null && userInfo.getSeasonMemberTime().getTime() > System.currentTimeMillis()) {
                    //会员没有到期，加上续费会员时间
                    userInfo.setSeasonMemberTime(DateUtils.getDate_add(userInfo.getYearMemberTime(), 3, 2));
                } else {
                    //会员到期
                    userInfo.setSeasonMemberTime(DateUtils.getDate_add(new Date(), 3, 2));
                }
                if (userInfo.getIsMember() < 4) {
                    userInfo.setIsMember(4);
                }
                userMoney.setRefreshNum(userMoney.getRefreshNum() + 18);
            }
            if (userOrder.getOrderMold() == 4) {
                //年充值
                if (userInfo.getYearMemberTime() != null && userInfo.getYearMemberTime().getTime() > System.currentTimeMillis()) {
                    //会员没有到期，加上续费会员时间
                    userInfo.setYearMemberTime(DateUtils.getDate_add(userInfo.getYearMemberTime(), 1, 3));
                } else {
                    //会员到期
                    userInfo.setYearMemberTime(DateUtils.getDate_add(new Date(), 1, 3));
                }
                userInfo.setIsMember(5);
                userMoney.setRefreshNum(userMoney.getRefreshNum() + 88);
            }
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        } else {
            //账户充值
            userMoney.setRepaidBalance(userMoney.getRepaidBalance().add(userOrder.getMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            userMoneyMapper.updateMoney(userMoney);
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userOrder.getUserId());
            userMoneyDetails.setType(3);
            userMoneyDetails.setIntroduce("充值");
            userMoneyDetails.setMoney(userOrder.getMoney());
            userMoneyDetails.setTradeTime(new Date());
            userMoneyMapper.insertMoneyDetails(userMoneyDetails);
        }
    }

    /**
     * 查询用户账户明细
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse<PageVO<UserMoneyDetails>> findDetails(Integer userId, Integer pageNo, Integer pageSize) {
        Page<UserMoneyDetails> page = PageHelper.startPage(pageNo, pageSize);
        userMoneyMapper.findAll(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }
}
