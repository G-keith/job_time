package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.ServiceFee;
import com.job.service.SignInMoneyService;
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

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/8
 */
@Slf4j
@RestController
@Api(tags = "用户签到及邀请金钱设置接口")
@RequestMapping(value = "/sign")
public class SignInMoneyController {

    @Autowired
    private SignInMoneyService signInMoneyService;

    @GetMapping("/all")
    @ApiOperation(value = "查询签到及邀请金钱信息")
    public ServerResponse findAll(){
        return signInMoneyService.findAll();
    }

    @PutMapping
    @ApiOperation(value = "更新签到及邀请金钱信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money", value = "签到金钱",required = true),
            @ApiImplicitParam(name = "inviteMoney", value = "邀请金钱",required = true),
    })
    public ServerResponse update(BigDecimal money, BigDecimal inviteMoney){
        return signInMoneyService.update(money, inviteMoney);
    }
}
