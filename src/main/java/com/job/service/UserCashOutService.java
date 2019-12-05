package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.AlipayUtils;
import com.job.common.utils.RandomUtil;
import com.job.common.utils.WxUtils;
import com.job.entity.*;
import com.job.entity.vo.CashOutOrderVo;
import com.job.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCashOutService {

    private final UserCashOutMapper userCashOutMapper;

    private final UserMoneyMapper userMoneyMapper;

    private final WxUtils wxUtils;

    private final AlipayUtils alipayUtils;

    private final UserInfoMapper userInfoMapper;

    private final JobMapper jobMapper;

    private final CashOutOrderMapper cashOutOrderMapper;

    public UserCashOutService(UserCashOutMapper userCashOutMapper, UserMoneyMapper userMoneyMapper, WxUtils wxUtils, AlipayUtils alipayUtils, UserInfoMapper userInfoMapper, JobMapper jobMapper, CashOutOrderMapper cashOutOrderMapper) {
        this.userCashOutMapper = userCashOutMapper;
        this.userMoneyMapper = userMoneyMapper;
        this.wxUtils = wxUtils;
        this.alipayUtils = alipayUtils;
        this.userInfoMapper = userInfoMapper;
        this.jobMapper = jobMapper;
        this.cashOutOrderMapper = cashOutOrderMapper;
    }

    /**
     * 查询所有提现申请
     *
     * @param userCashOut
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findAll(UserCashOut userCashOut, Integer pageNo, Integer pageSize) {
        Page<UserCashOut> page = PageHelper.startPage(pageNo, pageSize);
        userCashOutMapper.findAll(userCashOut);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询用户提现申请
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        Page<UserCashOut> page = PageHelper.startPage(pageNo, pageSize);
        userCashOutMapper.findByUserId(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 审核提现申请
     *
     * @param cashOutId
     * @param auditStatus
     * @param refuseReason
     * @return
     */
    public ServerResponse updateCashOut(Integer cashOutId, Integer auditStatus, String refuseReason, HttpServletRequest request) {
        UserCashOut userCashOut = userCashOutMapper.selectByPrimaryKey(cashOutId);
        System.out.println(userCashOut.getUserId());
        UserInfo userInfo = userInfoMapper.findByUserId(userCashOut.getUserId());
        ServerResponse serverResponse;
        String tradeNo = RandomUtil.getTimestamp() + RandomUtil.randomStr(3);
        userCashOut.setAuditStatus(auditStatus);
        userCashOut.setAuditTime(new Date());
        int result = 0;
        if (auditStatus == 3) {
            CashOutOrder cashOutOrder = new CashOutOrder();
            cashOutOrder.setRemarks("小蜜蜂提现");
            cashOutOrder.setTotalFee(userCashOut.getMakeMoney());
            cashOutOrder.setTradeNo(tradeNo);
            cashOutOrder.setUserId(userInfo.getUserId());
            if (userCashOut.getTradeType() == 1) {
                //支付宝提现
                cashOutOrder.setZfbName(userCashOut.getZfbName());
                cashOutOrder.setZfbAccount(userCashOut.getZfbAccount());
               // serverResponse = alipayUtils.cashOut(cashOutOrder);
                //todo 等代账户，先手动转账
                serverResponse = ServerResponse.createBySuccess();
            } else {
                //微信提现
                cashOutOrder.setOpenid(userInfo.getOpenid());
                // serverResponse = wxUtils. cashOut(cashOutOrder, request);
                serverResponse = ServerResponse.createBySuccess();
            }
            if (serverResponse.getStatus() == 1) {
                //审核通过，系统账户减去提现金钱
                userMoneyMapper.updateAdmin(userMoneyMapper.money().subtract(userCashOut.getMakeMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
                //成功,插入打款记录
                cashOutOrder.setPayTime(new Date());
                cashOutOrderMapper.insertSelective(cashOutOrder);
                //更新提现申请
                result = userCashOutMapper.updateByPrimaryKeySelective(userCashOut);
            }
        } else {
            //审核拒绝
            UserMoney userMoney = userMoneyMapper.selectById(userCashOut.getUserId());
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userCashOut.getUserId());
            userMoneyDetails.setIntroduce("审核拒绝,退还");
            userMoneyDetails.setMoney(userCashOut.getCashOutMoney());
            userMoneyDetails.setTradeTime(new Date());
            if (userCashOut.getCashOutType() == 1) {
                userMoneyDetails.setType(1);
                userMoney.setBond(userMoney.getBond().add(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (userCashOut.getCashOutType() == 2) {
                userMoneyDetails.setType(2);
                userMoney.setBalance(userMoney.getBalance().add(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));

            }
            if (userCashOut.getCashOutType() == 3) {
                userMoneyDetails.setType(3);
                userMoney.setRepaidBalance(userMoney.getRepaidBalance().add(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (userCashOut.getCashOutType() == 4) {
                userMoneyDetails.setType(4);
                userMoney.setBonus(userMoney.getBonus().add(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            userMoneyMapper.updateMoney(userMoney);
            //审核拒绝，系统账户减去提现金钱
            userMoneyMapper.updateAdmin(userMoneyMapper.money().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //插入明细
            userMoneyMapper.insertMoneyDetails(userMoneyDetails);
            userCashOut.setRefuseReason(refuseReason);
            result = userCashOutMapper.updateByPrimaryKeySelective(userCashOut);
        }
        //更新提现申请
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 插入提现申请
     *
     * @param userCashOut
     * @return
     */
    public ServerResponse insertCashOut(UserCashOut userCashOut) {
        //查询用户有没有提现过
        if (userCashOutMapper.countNow(userCashOut.getUserId(), userCashOut.getCashOutType()) == 1) {
            return ServerResponse.createByErrorMessage("您今天已经申请过了");
        }
        //去除账户里的钱
        UserMoney userMoney = userMoneyMapper.selectById(userCashOut.getUserId());
        //插入明细
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userCashOut.getUserId());
        userMoneyDetails.setIntroduce("提现申请");
        userMoneyDetails.setMoney(userCashOut.getCashOutMoney().negate().setScale(2, BigDecimal.ROUND_HALF_UP));
        userMoneyDetails.setTradeTime(new Date());
        if (userCashOut.getCashOutType() == 1) {
            if (userCashOut.getCashOutMoney().compareTo(userMoney.getBond()) > 0) {
                return ServerResponse.createByErrorMessage("余额不足");
            }
            userMoneyDetails.setType(1);
            userMoney.setBond(userMoney.getBond().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (userCashOut.getCashOutType() == 2) {
            if (userCashOut.getCashOutMoney().compareTo(userMoney.getBalance()) > 0) {
                return ServerResponse.createByErrorMessage("余额不足");
            }
            userCashOut.setMakeMoney(userCashOut.getCashOutMoney().subtract(getServiceFee(userCashOut.getUserId(),jobMapper.findCashOut(),userCashOut.getCashOutMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
            userMoneyDetails.setType(2);
            userMoney.setBalance(userMoney.getBalance().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));

        }
        if (userCashOut.getCashOutType() == 3) {
            if (userCashOut.getCashOutMoney().compareTo(userMoney.getRepaidBalance()) > 0) {
                return ServerResponse.createByErrorMessage("余额不足");
            }
            userCashOut.setMakeMoney(userCashOut.getCashOutMoney().subtract(getServiceFee(userCashOut.getUserId(),jobMapper.findRecharge(),userCashOut.getCashOutMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
            userMoneyDetails.setType(3);
            userMoney.setRepaidBalance(userMoney.getRepaidBalance().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (userCashOut.getCashOutType() == 4) {
            if (userCashOut.getCashOutMoney().compareTo(userMoney.getBonus()) > 0) {
                return ServerResponse.createByErrorMessage("余额不足");
            }
            userCashOut.setMakeMoney(userCashOut.getCashOutMoney().subtract(getServiceFee(userCashOut.getUserId(),jobMapper.findCashOut(),userCashOut.getCashOutMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
            userMoneyDetails.setType(4);
            userMoney.setBonus(userMoney.getBonus().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        int result = userCashOutMapper.insertSelective(userCashOut);
        userMoneyMapper.updateMoney(userMoney);
        //系统账户加上申请提现的钱
        userMoneyMapper.updateAdmin(userMoneyMapper.money().add(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
        //插入明细
        userMoneyMapper.insertMoneyDetails(userMoneyDetails);
        return ServerResponse.createBySuccess();
    }

    /**
     * 获取提现服务费
     * @param userId
     * @param serviceFee
     * @param cashOutMoney
     * @return
     */
    public BigDecimal getServiceFee(Integer userId,ServiceFee serviceFee,BigDecimal cashOutMoney){
        UserInfo userInfo=userInfoMapper.findByUserId(userId);
        if(userInfo.getIsMember()==2){
            return cashOutMoney.multiply(serviceFee.getWeekRate()).setScale(2,BigDecimal.ROUND_HALF_UP);
        }else if(userInfo.getIsMember()==3){
            return cashOutMoney.multiply(serviceFee.getMonthRate()).setScale(2,BigDecimal.ROUND_HALF_UP);
        }else if(userInfo.getIsMember()==4){
            return cashOutMoney.multiply(serviceFee.getSeasonRate()).setScale(2,BigDecimal.ROUND_HALF_UP);
        }else if(userInfo.getIsMember()==5){
            return cashOutMoney.multiply(serviceFee.getYearRate()).setScale(2,BigDecimal.ROUND_HALF_UP);
        }else{
            return cashOutMoney.multiply(serviceFee.getCommonRate()).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 查询所有打款记录
     *
     * @return
     */
    public ServerResponse findCashOut(Integer pageNo, Integer pageSize, String phone, String nickName) {
        Page<CashOutOrderVo> page = PageHelper.startPage(pageNo, pageSize);
        cashOutOrderMapper.findCashOut(phone, nickName);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }
}
