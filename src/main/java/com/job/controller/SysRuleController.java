package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.SysRule;
import com.job.entity.vo.SysRuleDetailsVo;
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
@Api(tags = "后台规则说明接口")
@RequestMapping(value = "/rule")
public class SysRuleController {

    @Autowired
    private SysRuleService sysRuleService;

    @GetMapping("/all")
    @ApiOperation(value = "查询所有平台规则信息")
    public ServerResponse findAll(){
        return sysRuleService.findAll();
    }

    @GetMapping("/id")
    @ApiOperation(value = "查询规则列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ruleId", value = "主键id", dataType = "int",required = true),
    })
    public ServerResponse findDetails(Integer ruleId){
        return sysRuleService.findDetails(ruleId);
    }

    @PutMapping("/id")
    @ApiOperation("更新规则列表信息")
    public ServerResponse updateRuleDetails(@RequestBody SysRuleDetailsVo sysRuleDetailsVo){
        return sysRuleService.updateRule(sysRuleDetailsVo);
    }

}
