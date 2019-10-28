package com.job.controller.user;

import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.MD5Util;
import com.job.entity.user.UserInfo;
import com.job.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/sign-in")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "账号", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", required = true),
    })
    public ServerResponse signIn(String loginName, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName(loginName);
        userInfo.setPassword(MD5Util.md5EncodeUtf8(password));
        userInfo.setIsAdmin(0);
        return userInfoService.signIn(userInfo);
    }

    @GetMapping("/register")
    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "账号", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", required = true),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "string", required = true),
            @ApiImplicitParam(name = "head", value = "头像路径", dataType = "string", required = true),
    })
    public ServerResponse register(String loginName, String password, String phone, String nickName, String head) {
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName(loginName);
        userInfo.setPassword(MD5Util.md5EncodeUtf8(password));
        userInfo.setPhone(phone);
        userInfo.setNickName(nickName);
        userInfo.setHead(head);
        return userInfoService.insertUserInfo(userInfo);
    }

    @PostMapping("/checkPhone")
    @ApiOperation("校验手机号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
    })
    public ServerResponse checkPhone(String phone) {
        return userInfoService.checkPhone(phone);
    }

    @PostMapping("/checkNickName")
    @ApiOperation("校验昵称是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "string", required = true),
    })
    public ServerResponse checkNickName(String nickName) {
        return userInfoService.checkNickName(nickName);
    }

    @PutMapping("/modifyPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "string", required = true),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
    })
    public ServerResponse modifyPassword(String newPassword, String phone) {
        return userInfoService.updatePassword(newPassword, phone);
    }

    @GetMapping("code")
    @ApiOperation("获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
    })
    public ServerResponse getCode(String phone) {
        return userInfoService.insertCode(phone);
    }



}
