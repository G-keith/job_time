package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.Job;
import com.job.service.JobService;
import com.job.service.UserJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/29
 */
@Slf4j
@RestController
@Api(tags = "发布管理")
@RequestMapping(value = "/job")
public class JobController {

    private final JobService jobService;

    private final UserJobService userJobService;

    public JobController(JobService jobService, UserJobService userJobService) {
        this.jobService = jobService;
        this.userJobService = userJobService;
    }

    @GetMapping("/user")
    @ApiOperation(value = "查询用户所有提交的任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "status", value = "状态（1.已报名；2.已过期；3.已提交；4.审核通过；5.审核拒绝；）", dataType = "int"),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findByUserId(Integer userId, Integer status, Integer pageNo, Integer pageSize) {
        return userJobService.findByUserId(userId, status, pageNo, pageSize);
    }

    @GetMapping("/footprint")
    @ApiOperation(value = "查询用户足迹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findFootprint(Integer userId, Integer pageNo, Integer pageSize) {
        return jobService.findFootprint(userId, pageNo, pageSize);
    }

    @GetMapping("/release")
    @ApiOperation(value = "查询用户所发布的任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态（2.审核中，3审核通过；4；审核拒绝）", dataType = "int"),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findRelease(Integer userId, Integer auditStatus, Integer pageNo, Integer pageSize) {
        return jobService.findRelease(userId, auditStatus, pageNo, pageSize);
    }

    @GetMapping("/end")
    @ApiOperation(value = "查询用户已结束的任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findEndRelease(Integer userId,Integer pageNo, Integer pageSize) {
        return jobService.findEndRelease(userId, pageNo, pageSize);
    }

    @PutMapping("/suspend-end")
    @ApiOperation(value = "暂停,开始或结束任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobId", value = "任务id", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "类型（1，开始 2.结束，3.暂停）", dataType = "int", required = true),
    })
    public ServerResponse suspendOrEnd(Integer jobId, Integer type) {
        return jobService.suspendOrEnd(jobId, type);
    }

//    @GetMapping("/audit")
//    @ApiOperation(value = "查询需要审核的任务列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
//            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
//            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
//    })
//    public ServerResponse findWillAudit(Integer userId, Integer pageNo, Integer pageSize) {
//        return jobService.findWillAudit(userId, pageNo, pageSize);
//    }

    @GetMapping("/userJob")
    @ApiOperation(value = "查询待审核的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "status", value = "3.待审核；4.审核通过；5.审核拒绝", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
    })
    public ServerResponse findUserJob(Integer status, Integer pageNo, Integer pageSize,Integer userId) {
        return jobService.findUserJob(status, pageNo, pageSize,userId);
    }

    @GetMapping("/checkPicture")
    @ApiOperation(value = "查询用户提交的验证图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "验证主键id", dataType = "int", required = true),
    })
    public ServerResponse findCheckPicture(Integer taskId) {
        return jobService.findCheckPicture(taskId);
    }

    @PostMapping
    @ApiOperation(value = "插入用户发布的任务")
    public ServerResponse insertJob(@RequestBody Job job) {
        return jobService.insertJob(job);
    }

    @PutMapping("/userJob")
    @ApiOperation(value = "插入审核结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "验证主键id", dataType = "int", required = true),
            @ApiImplicitParam(name = "status", value = "审核结果（4.审核通过；5.审核拒绝）", dataType = "int", required = true),
            @ApiImplicitParam(name = "refuseReason", value = "不通过理由", dataType = "string"),
    })
    public ServerResponse updateUserJob(Integer taskId, Integer status, String refuseReason) {
        return jobService.updateUserJob(taskId, status, refuseReason);
    }

    @PutMapping("/refreshJob")
    @ApiOperation(value = "刷新任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobId", value = "任务id", dataType = "int", required = true),})
    public ServerResponse refreshJob(Integer jobId) {
        return jobService.refreshJob(jobId);
    }
}
