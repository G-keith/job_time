package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserInfo;
import com.job.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Slf4j
@RestController
@Api(tags = "")
@RequestMapping(value = "/user/admin")
public class AdminUserInfoController {

    private final UserInfoService userInfoService;

    public AdminUserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    @GetMapping("/all")
    @ApiOperation(value = "获取所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "状态（0正常，1黑名单）", dataType = "nt"),
    })
    public ServerResponse findAll(Integer pageNo,Integer pageSize,String phone,Integer status){
        return userInfoService.findAll(pageNo, pageSize,phone,status);
    }

    @PutMapping
    @ApiOperation(value = "用户加入黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int",required = true),
            @ApiImplicitParam(name = "reason", value = "原因", dataType = "string",required = true),
    })
    public ServerResponse updateUserInfo(Integer userId,String reason){
        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setReason(reason);
        return userInfoService.updateUserInfo(userInfo);
    }
}
