package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserCashOut;
import com.job.service.UserCashOutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/14
 */
@Slf4j
@RestController
@Api(tags = "用户提现接口")
@RequestMapping(value = "/cashOut")
public class UserCashOutController {

    @Autowired
    private UserCashOutService userCashOutService;

    @GetMapping("/all")
    @ApiOperation(value = "获取所有提现申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "cashOutType", value = "提现类型（1.保证金；2.任务余额；3.充值余额；4.奖金余额）", dataType = "int"),
            @ApiImplicitParam(name = "tradeType", value = "交易方式（1.支付宝。2.微信）", dataType = "int"),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态（1.审核中；2.审核拒绝；3.审核通过）", dataType = "int"),
    })
    public ServerResponse findAll(Integer pageNo, Integer pageSize, String phone, Integer cashOutType, Integer tradeType, Integer auditStatus) {
        UserCashOut userCashOut = new UserCashOut();
        if (phone != null) {
            userCashOut.setPhone(phone);
        }
        if (cashOutType != null) {
            userCashOut.setCashOutType(cashOutType);
        }
        if (tradeType != null) {
            userCashOut.setTradeType(tradeType);
        }
        if (auditStatus != null) {
            userCashOut.setAuditStatus(auditStatus);
        }
        return userCashOutService.findAll(userCashOut, pageNo, pageSize);
    }

    @GetMapping
    @ApiOperation(value = "获取用户提现申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
    })
    public ServerResponse findByUserId(Integer pageNo, Integer pageSize, Integer userId) {
        return userCashOutService.findByUserId(userId, pageNo, pageSize);
    }

    @PostMapping
    @ApiOperation(value = "插入提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "cashOutType", value = "提现类型（1.保证金；2.任务余额；3.充值余额；4.奖金余额）", dataType = "int", required = true),
            @ApiImplicitParam(name = "tradeType", value = "交易方式（1.支付宝。2.微信）", dataType = "int", required = true),
            @ApiImplicitParam(name = "cashOutMoney", value = "提现金额", dataType = "int", required = true),
            @ApiImplicitParam(name = "zfbName", value = "支付宝姓名（选择支付宝提现时必传）", dataType = "string"),
            @ApiImplicitParam(name = "zfbAccount", value = "支付宝账户（选择支付宝提现时必传）", dataType = "string"),
    })
    public ServerResponse insertCashOut(Integer userId, Integer cashOutType, Integer tradeType, BigDecimal cashOutMoney,
                                        String zfbName, String zfbAccount) {
        UserCashOut userCashOut = new UserCashOut();
        userCashOut.setUserId(userId);
        userCashOut.setCashOutType(cashOutType);
        userCashOut.setTradeType(tradeType);
        userCashOut.setCashOutMoney(cashOutMoney);
        userCashOut.setApplyTime(new Date());
        if (zfbName != null) {
            userCashOut.setZfbName(zfbName);
        }
        if (zfbAccount != null) {
            userCashOut.setZfbAccount(zfbAccount);
        }
        return userCashOutService.insertCashOut(userCashOut);
    }

    @PutMapping
    @ApiOperation(value = "插入审核结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cashOutId", value = "主键id", dataType = "int", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "2.审核拒绝；3.审核通过", dataType = "int", required = true),
            @ApiImplicitParam(name = "refuseReason", value = "拒绝原因（拒绝时必传）", dataType = "string"),
    })
    public ServerResponse updateCashOut(Integer cashOutId, Integer auditStatus, String refuseReason, HttpServletRequest request){
        return userCashOutService.updateCashOut(cashOutId, auditStatus, refuseReason,request);
    }
}
