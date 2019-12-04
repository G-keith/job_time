package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.AlipayUtils;
import com.job.common.utils.WxUtils;
import com.job.entity.CashOutOrder;
import com.job.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Slf4j
@RestController
@Api(tags = "手机端账号信息接口")
@RequestMapping(value = "/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    private final WxUtils wxUtils;

    private final AlipayUtils alipayUtils;

    public UserInfoController(UserInfoService userInfoService, WxUtils wxUtils, AlipayUtils alipayUtils) {
        this.userInfoService = userInfoService;
        this.wxUtils = wxUtils;
        this.alipayUtils = alipayUtils;
    }

    @GetMapping("/enroll")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "uid", value = "邀请人uid", dataType = "string"),
    })
    public ServerResponse signIn(String phone, String uid) {
        return userInfoService.signIn(phone, uid);
    }

    @GetMapping("/weChatLogin")
    @ApiOperation(value = "微信授权登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "用户换取access_token的code，仅在ErrCode为0时有效", required = true, dataType = "String")
    })
    public ServerResponse weChatLogin(String code) throws IOException {
        return userInfoService.weChatLogin(code);
    }

    @GetMapping("code")
    @ApiOperation("获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
    })
    public ServerResponse getCode(String phone) {
        return userInfoService.insertCode(phone);
    }

    @GetMapping("checkCode")
    @ApiOperation("检验验证码是否正确")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", required = true),
    })
    public ServerResponse checkCode(String phone, String code) {
        return userInfoService.checkCode(phone, code);
    }

    @GetMapping("/black")
    @ApiOperation(value = "获取黑名单用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string"),
    })
    public ServerResponse findBlacklist(Integer pageNo, Integer pageSize, String phone) {
        return userInfoService.findBlacklist(pageNo, pageSize, phone);
    }

    @GetMapping("/wxRecharge")
    @ApiOperation(value = "获取微信充值预支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "money", value = "金额", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "类型（1.代表会员充值，2代表账户充值）", dataType = "int", required = true),
            @ApiImplicitParam(name = "mold", value = "1.周充值，2.月充值，3.季充值，4.年充值", dataType = "int", required = true),
    })
    public ServerResponse wxRecharge(Integer userId, BigDecimal money, Integer type,Integer mold ,HttpServletRequest request) throws IOException {
        return userInfoService.wxRecharge(userId, money, type, request,mold);
    }

    @GetMapping("/zfbRecharge")
    @ApiOperation(value = "获取支付宝充值信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "money", value = "金额", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "类型（1.代表会员充值，2代表账户充值）", dataType = "int", required = true),
            @ApiImplicitParam(name = "mold", value = "1.周充值，2.月充值，3.季充值，4.年充值", dataType = "int", required = true),

    })
    public ServerResponse zfbRecharge(Integer userId, BigDecimal money, Integer type,Integer mold) {
        return userInfoService.zfbRecharge(userId, money, type,mold);
    }

    @PutMapping("/headimgurl")
    @ApiOperation(value = "更新头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "headimgurl", value = "头像路径", dataType = "string", required = true),
    })
    public ServerResponse headimgurl(String headimgurl, Integer userId) {
        return userInfoService.headimgurl(headimgurl, userId);
    }
}
