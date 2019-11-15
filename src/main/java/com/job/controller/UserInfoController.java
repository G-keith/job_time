package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.WxUtils;
import com.job.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    public UserInfoController(UserInfoService userInfoService, WxUtils wxUtils) {
        this.userInfoService = userInfoService;
        this.wxUtils = wxUtils;
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

    @GetMapping("/openOrRenew")
    @ApiOperation(value = "会员充值或者续费")
    public ServerResponse openOrRenew() {
        return null;
    }

}
