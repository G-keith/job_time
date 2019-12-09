package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/29
 */
@Slf4j
@RestController
@Api(tags = "后台任务审核接口")
@RequestMapping(value = "/admin/job")
public class AdminJobController {

    private final JobService jobService;

    public AdminJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "获得所有待审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="jobTitle",value = "任务标题",dataType = "string"),
            @ApiImplicitParam(name ="jobSource",value = "项目名称",dataType = "string"),
            @ApiImplicitParam(name ="typeId",value = "项目类型",dataType = "int"),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findAll(String jobTitle,String jobSource,Integer typeId,Integer pageNo,Integer pageSize){
        return jobService.findAll( jobTitle,jobSource,typeId,pageNo,pageSize);
    }

    @GetMapping("/step")
    @ApiOperation("查询任务步骤详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="jobId",value = "任务主键id",dataType = "int",required = true),
    })
    public ServerResponse findStep(Integer jobId){
        return jobService.findAllStep(jobId);
    }

    @PutMapping
    @ApiOperation(value = "插入审核结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="jobId",value = "任务主键id",dataType = "int",required = true),
            @ApiImplicitParam(name ="auditStatus",value = "审核结果（通过传3，拒绝传4）",dataType = "int",required = true),
            @ApiImplicitParam(name ="refuseReason",value = "拒绝原因",dataType = "string"),
            @ApiImplicitParam(name ="isRecommend",value = "是否推荐（1不推荐，2推荐）",dataType = "int"),
    })
    public ServerResponse updateJob(Integer jobId,Integer auditStatus,String refuseReason,Integer isRecommend){
        return jobService.updateJob(jobId, auditStatus,refuseReason,isRecommend);
    }
}
