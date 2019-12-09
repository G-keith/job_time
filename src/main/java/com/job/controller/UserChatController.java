package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.UserChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/9
 */
@Slf4j
@RestController
@Api(tags = "聊天接口")
@RequestMapping(value = "/chat")
public class UserChatController {

    private final UserChatService userChatService;

    public UserChatController(UserChatService userChatService) {
        this.userChatService = userChatService;
    }

    @PostMapping("/record")
    @ApiOperation(value = "插入聊天记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "targetId", value = "聊天对象id", dataType = "int", required = true),
            @ApiImplicitParam(name = "newsContent", value = "内容（不允许输入空）", dataType = "string", required = true),
    })
    public ServerResponse insertChatRecord(Integer userId,Integer targetId,String newsContent){
        return userChatService.insetChatRecord(userId, targetId, newsContent);
    }

    @GetMapping("/record")
    @ApiOperation(value = "查询聊天记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "targetId", value = "聊天对象id", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse selectChatRecord(Integer pageNo, Integer pageSize,Integer userId,Integer targetId){
        return userChatService.selectChatRecord(pageNo, pageSize, userId, targetId);
    }

    @GetMapping
    @ApiOperation(value = "查询消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse selectNews(Integer pageNo, Integer pageSize,Integer userId){
        return userChatService.selectNews(pageNo, pageSize, userId);
    }
}
