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


    public UserMoneyService(UserMoneyMapper userMoneyMapper, UserOrderMapper userOrderMapper, UserInfoMapper userInfoMapper) {
        this.userMoneyMapper = userMoneyMapper;
        this.userOrderMapper = userOrderMapper;
        this.userInfoMapper = userInfoMapper;
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
        if (userOrder.getOrderType() == 1) {
            //会员充值
            UserInfo userInfo = userInfoMapper.findByUserId(userOrder.getUserId());
            if (userInfo.getIsMember() == 2 && userInfo.getMemberTime().getTime() > System.currentTimeMillis()) {
                //会员没有到期，加上续费会员时间
                userInfo.setMemberTime(DateUtils.getDate_add(userInfo.getMemberTime(), 30, 1));
            } else {
                //会员到期
                userInfo.setMemberTime(DateUtils.getDate_add(new Date(), 30, 1));
                userInfo.setIsMember(2);
            }
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        } else {
            //账户充值
            UserMoney userMoney = userMoneyMapper.selectById(userOrder.getUserId());
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
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse<PageVO<UserMoneyDetails>> findDetails(Integer userId,Integer pageNo,Integer pageSize){
        Page<UserMoneyDetails> page = PageHelper.startPage(pageNo, pageSize);
        userMoneyMapper.findAll(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }
}
