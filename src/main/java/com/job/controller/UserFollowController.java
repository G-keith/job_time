package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.UserFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/12
 */
@Slf4j
@RestController
@Api(tags = "用户关注接口")
@RequestMapping(value = "/follow")
public class UserFollowController {

    private final UserFollowService userFollowService;

    public UserFollowController(UserFollowService userFollowService) {
        this.userFollowService = userFollowService;
    }

    @GetMapping("/isFollow")
    @ApiOperation(value = "查询用户是否关注过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int",required = true),
            @ApiImplicitParam(name = "followId", value = "被关注者id", dataType = "int",required = true),
    })
    public ServerResponse isFollow(Integer userId,Integer followId){
        return userFollowService.isFollow(userId, followId);
    }

    @PostMapping
    @ApiOperation(value = "插入用户关注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int",required = true),
            @ApiImplicitParam(name = "followId", value = "被关注者id", dataType = "int",required = true),
    })
    public ServerResponse insertFollow(Integer userId,Integer followId){
        return userFollowService.insertFollow(userId, followId);
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户关注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int",required = true),
            @ApiImplicitParam(name = "followId", value = "被关注者id", dataType = "int",required = true),
    })
    public ServerResponse deleteFollow(Integer userId,Integer followId){
        return userFollowService.deleteFollow(userId, followId);
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询所有用户关注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int",required = true),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findAll(Integer userId,Integer pageNo,Integer pageSize){
        return userFollowService.findAll(userId,pageNo,pageSize);
    }
}
