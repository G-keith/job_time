package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserOpinion;
import com.job.service.UserOpinionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/23
 */
@Slf4j
@RestController
@Api(tags = "用户意见反馈")
@RequestMapping(value = "/opinion")
public class UserOpinionController {

    @Autowired
    private UserOpinionService userOpinionService;

    @PostMapping
    @ApiOperation(value = "插入用户反馈意见")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "opinion", value = "反馈意见", dataType = "string", required = true),
    })
    public ServerResponse insertOpinion(Integer userId,String opinion){
        UserOpinion userOpinion=new UserOpinion();
        userOpinion.setUserId(userId);
        userOpinion.setOpinion(opinion);
        userOpinion.setCommitTime(new Date());
        return userOpinionService.insertSelective(userOpinion);
    }

    @PutMapping
    @ApiOperation(value = "更新用户反馈意见")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "opinionId", value = "主键id", dataType = "int", required = true),
            @ApiImplicitParam(name = "reply", value = "回复", dataType = "string", required = true),
    })
    public ServerResponse updateOpinion(Integer opinionId,String reply){
        UserOpinion userOpinion=new UserOpinion();
        userOpinion.setOpinionId(opinionId);
        userOpinion.setReply(reply);
        userOpinion.setReplyTime(new Date());
        userOpinion.setStatus(2);
        return userOpinionService.updateByPrimaryKeySelective(userOpinion);
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询用户反馈列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "1.未处理，2已处理", dataType = "int"),
    })
    public ServerResponse findAll(String phone,String nickName,Integer userId,Integer status,Integer pageNo, Integer pageSize){
        return userOpinionService.findAll(phone,nickName,userId, status, pageNo, pageSize);
    }
}
