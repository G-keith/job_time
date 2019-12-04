package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.UserCashOutService;
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
 * @date 2019/12/3
 */
@Slf4j
@RestController
@Api(tags = "打款记录接口")
@RequestMapping(value = "/cash-out-order")
public class CashOutOrderController {

    @Autowired
    private UserCashOutService userCashOutService;

    @GetMapping
    @ApiOperation(value = "打款记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "string"),
    })
    public ServerResponse findCashOut(Integer pageNo,Integer pageSize,String phone,String nickName){
        return userCashOutService.findCashOut(pageNo, pageSize,phone,nickName);
    }
}
