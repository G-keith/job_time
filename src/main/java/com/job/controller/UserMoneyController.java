package com.job.controller;

import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserMoneyDetails;
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


    @GetMapping("/details")
    @ApiOperation(value = "查询用户账户明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse<PageVO<UserMoneyDetails>> findDetails(Integer userId, Integer pageNo, Integer pageSize){
        return userMoneyService.findDetails(userId, pageNo, pageSize);
    }

}
