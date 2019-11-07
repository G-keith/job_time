package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.ExtendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Slf4j
@RestController
@Api(tags = "推广接口")
@RequestMapping(value = "/extend")
public class ExtendController {

    private final ExtendService extendService;

    public ExtendController(ExtendService extendService) {
        this.extendService = extendService;
    }

    @GetMapping("/user")
    @ApiOperation(value = "查询用户邀请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int",required = true),
    })
    public ServerResponse selectUser(Integer userId){
        return extendService.selectUser(userId);
    }

    @GetMapping("/invite")
    @ApiOperation(value = "本月邀请排行榜")
    public ServerResponse countInvite(){
        return extendService.countInvite();
    }
}
