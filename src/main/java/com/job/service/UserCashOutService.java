package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserCashOut;
import com.job.entity.UserMoney;
import com.job.entity.UserMoneyDetails;
import com.job.entity.vo.JobListVo;
import com.job.mapper.UserCashOutMapper;
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
 * @date 2019/11/14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCashOutService {

    private final UserCashOutMapper userCashOutMapper;

    private final UserMoneyMapper userMoneyMapper;

    public UserCashOutService(UserCashOutMapper userCashOutMapper, UserMoneyMapper userMoneyMapper) {
        this.userCashOutMapper = userCashOutMapper;
        this.userMoneyMapper = userMoneyMapper;
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
    public ServerResponse findByUserId(Integer userId,Integer pageNo, Integer pageSize) {
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
    public ServerResponse updateCashOut(Integer cashOutId, Integer auditStatus, String refuseReason) {
        UserCashOut userCashOut = userCashOutMapper.selectByPrimaryKey(cashOutId);
        if (auditStatus == 2) {
            if (userCashOut.getTradeType() == 1) {
                //支付宝提现
            }
            if (userCashOut.getTradeType() == 2) {
                //微信提现
            }
            //审核通过，系统账户减去提现金钱
            userMoneyMapper.updateAdmin(userMoneyMapper.money().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            //审核拒绝
            UserMoney userMoney = userMoneyMapper.selectById(userCashOut.getUserId());
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userCashOut.getUserId());
            userMoneyDetails.setIntroduce("审核拒绝");
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
        }
        //更新提现申请
        userCashOut.setAuditStatus(auditStatus);
        if (refuseReason != null) {
            userCashOut.setRefuseReason(refuseReason);
        }
        userCashOut.setAuditTime(new Date());
        int result = userCashOutMapper.updateByPrimaryKeySelective(userCashOut);
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
        if(userCashOutMapper.countNow(userCashOut.getUserId())==1){
            return ServerResponse.createByErrorMessage("您今天已经申请过了");
        }
        int result = userCashOutMapper.insertSelective(userCashOut);
        if (result > 0) {
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
                userMoneyDetails.setType(2);
                userMoney.setBalance(userMoney.getBalance().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));

            }
            if (userCashOut.getCashOutType() == 3) {
                if (userCashOut.getCashOutMoney().compareTo(userMoney.getRepaidBalance()) > 0) {
                    return ServerResponse.createByErrorMessage("余额不足");
                }
                userMoneyDetails.setType(3);
                userMoney.setRepaidBalance(userMoney.getRepaidBalance().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (userCashOut.getCashOutType() == 4) {
                if (userCashOut.getCashOutMoney().compareTo(userMoney.getBonus()) > 0) {
                    return ServerResponse.createByErrorMessage("余额不足");
                }
                userMoneyDetails.setType(4);
                userMoney.setBonus(userMoney.getBonus().subtract(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            userMoneyMapper.updateMoney(userMoney);
            //系统账户加上申请提现的钱
            userMoneyMapper.updateAdmin(userMoneyMapper.money().add(userCashOut.getCashOutMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //插入明细
            userMoneyMapper.insertMoneyDetails(userMoneyDetails);
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("申请失败，请联系客服");
        }
    }
}
