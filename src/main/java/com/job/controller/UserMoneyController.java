package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.UserMoneyService;
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
 * @date 2019/11/13
 */
@Slf4j
@RestController
@Api(tags = "用户账户管理")
@RequestMapping(value = "/money")
public class UserMoneyController {

    @Autowired
    private UserMoneyService userMoneyService;


    @GetMapping("/all")
    @ApiOperation(value = "查询用户账户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
    })
    public ServerResponse findAll(Integer userId){
        return userMoneyService.findMoney(userId);
    }


}
