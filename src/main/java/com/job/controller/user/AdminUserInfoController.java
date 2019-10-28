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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Slf4j
@RestController
@Api(tags = "后台账号信息接口")
@RequestMapping(value = "/user/admin")
public class AdminUserInfoController {

    private final UserInfoService userInfoService;

    public AdminUserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/sign-in")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName",value = "账号",dataType = "string",required = true),
            @ApiImplicitParam(name = "password",value = "密码",dataType = "string",required = true),
    })
    public ServerResponse signIn(String loginName,String password){
        UserInfo userInfo=new UserInfo();
        userInfo.setLoginName(loginName);
        userInfo.setPassword(MD5Util.md5EncodeUtf8(password));
        userInfo.setIsAdmin(1);
        return userInfoService.signIn(userInfo);
    }
}
