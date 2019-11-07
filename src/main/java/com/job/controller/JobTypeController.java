package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.JobType;
import com.job.service.JobTypeService;
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
 * @date 2019/10/29
 */
@Slf4j
@RestController
@Api(tags = "后台任务类型管理接口")
@RequestMapping(value = "/jobType")
public class JobTypeController {

    private final JobTypeService jobTypeService;

    public JobTypeController(JobTypeService jobTypeService) {
        this.jobTypeService = jobTypeService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询所有任务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "15"),
    })
    public ServerResponse findAll(Integer pageNo, Integer pageSize) {
        return jobTypeService.findAll(pageNo, pageSize);
    }

    @PostMapping
    @ApiOperation(value = "添加任务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeName", value = "类型名称", dataType = "string", required = true),
            @ApiImplicitParam(name = "describe", value = "描述", dataType = "string"),
    })
    public ServerResponse add(String typeName, String describe) {
        JobType jobType = new JobType();
        jobType.setTypeName(typeName);
        if (describe.length() > 0) {
            jobType.setDescribe(describe);
        }
        return jobTypeService.add(jobType);
    }

    @PutMapping
    @ApiOperation(value = "修改任务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "主键id", dataType = "string", required = true),
            @ApiImplicitParam(name = "typeName", value = "类型名称", dataType = "string", required = true),
            @ApiImplicitParam(name = "describe", value = "描述", dataType = "string"),
    })
    public ServerResponse edit(Integer typeId, String typeName, String describe) {
        JobType jobType = new JobType();
        jobType.setTypeId(typeId);
        jobType.setTypeName(typeName);
        if (describe.length() > 0) {
            jobType.setDescribe(describe);
        }
        return jobTypeService.edit(jobType);
    }

    @DeleteMapping
    @ApiOperation(value = "删除任务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "主键id", dataType = "string", required = true),
    })
    public ServerResponse delete(Integer typeId) {
        return jobTypeService.delete(typeId);
    }
}
