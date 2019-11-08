package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.SysRule;
import com.job.service.SysRuleService;
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
 * @date 2019/11/8
 */
@Slf4j
@RestController
@Api(tags = "后台轮播图接口")
@RequestMapping(value = "/rule")
public class SysRuleController {

    @Autowired
    private SysRuleService sysRuleService;

    @GetMapping("/all")
    @ApiOperation(value = "查询所有规则信息")
    public ServerResponse findAll(){
        return sysRuleService.findAll();
    }

    @PutMapping
    @ApiOperation("更新规则信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ruleId", value = "主键id", dataType = "int",required = true),
            @ApiImplicitParam(name = "ruleType", value = "规则类型", dataType = "string",required = true),
            @ApiImplicitParam(name = "ruleIntroduce", value = "规则介绍", dataType = "string",required = true),
            @ApiImplicitParam(name = "remarks", value = "备注", dataType = "string"),
    })
    public ServerResponse updateRule(Integer ruleId,Integer ruleType,String ruleIntroduce,String remarks){
        SysRule sysRule=new SysRule();
        sysRule.setRuleId(ruleId);
        sysRule.setRuleIntroduce(ruleIntroduce);
        sysRule.setRuleType(ruleType);
        if(remarks!=null){
            sysRule.setRemarks(remarks);
        }
        return sysRuleService.updateRule(sysRule);
    }

    @PostMapping
    @ApiOperation(value = "插入规则信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ruleType", value = "规则类型", dataType = "string",required = true),
            @ApiImplicitParam(name = "ruleIntroduce", value = "规则介绍", dataType = "string",required = true),
            @ApiImplicitParam(name = "remarks", value = "备注", dataType = "string"),
    })
    public ServerResponse insertRule(Integer ruleType,String ruleIntroduce,String remarks){
        SysRule sysRule=new SysRule();
        sysRule.setRuleIntroduce(ruleIntroduce);
        sysRule.setRuleType(ruleType);
        if(remarks!=null){
            sysRule.setRemarks(remarks);
        }
        return sysRuleService.insertRule(sysRule);
    }
}
