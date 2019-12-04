package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.UserAgentService;
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
 * @date 2019/11/28
 */
@Slf4j
@RestController
@Api(tags = "经纪人接口")
@RequestMapping(value = "/rule")
public class UserAgentController {

    @Autowired
    private UserAgentService userAgentService;

    @PostMapping
    @ApiOperation(value = "插入富文本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "内容", dataType = "string", required = true)
    })
    public ServerResponse insertAgent(String content) {
        return userAgentService.insertAgent(content);
    }

    @GetMapping
    @ApiOperation(value = "查询富文本信息")
    public ServerResponse findAgent() {
        return userAgentService.findAgent();
    }
}
