package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserReport;
import com.job.service.UserReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/4
 */
@Slf4j
@RestController
@Api(tags = "举报信息接口")
@RequestMapping(value = "/report")
public class UserReportController {

    @Autowired
    private UserReportService userReportService;

    @GetMapping("/user/all")
    @ApiOperation(value = "查询用户举报的列表（未结束）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "状态（1.举报中；2.已回复；3.已提交）", dataType = "int", required = true),
    })
    public ServerResponse findAll(Integer pageNo, Integer pageSize, Integer reportStatus, Integer userId) {
        return userReportService.findAll(pageNo, pageSize, reportStatus, userId);
    }

    @GetMapping("/reward/all")
    @ApiOperation(value = "查询悬赏主被举报的列表（未结束）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "状态（1.未回复；2.已回复；3.已提交）", dataType = "int", required = true),
    })
    public ServerResponse findRewardAll(Integer pageNo, Integer pageSize, Integer reportStatus, Integer userId) {
        return userReportService.findRewardAll(pageNo, pageSize, reportStatus, userId);
    }

    @GetMapping("/user/audit")
    @ApiOperation(value = "查询用户举报的列表（已结束）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
    })
    public ServerResponse findUserAudit(Integer pageNo, Integer pageSize, Integer userId) {
        return userReportService.findUserAudit(pageNo, pageSize, userId);
    }

    @GetMapping("/reward/audit")
    @ApiOperation(value = "查询悬赏主被举报的列表（已结束）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
    })
    public ServerResponse findRewardAudit(Integer pageNo, Integer pageSize, Integer userId) {
        return userReportService.findRewardAudit(pageNo, pageSize, userId);
    }

    @GetMapping("/admin/all")
    @ApiOperation(value = "后台查询已经审核的举报列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findAudit(Integer pageNo, Integer pageSize) {
        return userReportService.findAudit(pageNo, pageSize);
    }

    @GetMapping("/admin/will")
    @ApiOperation(value = "后台查询待审核的举报列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
    })
    public ServerResponse findWillAudit(Integer pageNo, Integer pageSize) {
        return userReportService.findWillAudit(pageNo, pageSize);
    }

    @PostMapping
    @ApiOperation(value = "插入用户举报信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "taskId", value = "用户提交任务主键id", dataType = "int", required = true),
            @ApiImplicitParam(name = "reportReason", value = "举报原因", dataType = "string", required = true),
            @ApiImplicitParam(name = "reportImg", value = "举报图片", dataType = "string"),
    })
    public ServerResponse insertReport(Integer userId, Integer taskId, String reportReason,String reportImg) {
        UserReport userReport = new UserReport();
        userReport.setUserId(userId);
        userReport.setTaskId(taskId);
        userReport.setReportReason(reportReason);
        if(reportImg!=null){
            userReport.setReportImg(reportImg);
        }
        userReport.setReportTime(new Date());
        return userReportService.insertReport(userReport);
    }

    @PutMapping("/reply")
    @ApiOperation(value = "悬赏主回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportId", value = "主键id", dataType = "int", required = true),
            @ApiImplicitParam(name = "replyReason", value = "回复原因", dataType = "string", required = true),
            @ApiImplicitParam(name = "replyImg", value = "回复图片", dataType = "string"),
    })
    public ServerResponse replyReport(Integer reportId,String replyReason,String replyImg){
        UserReport userReport = new UserReport();
        userReport.setReportId(reportId);
        userReport.setReplyReason(replyReason);
        if(replyImg!=null){
            userReport.setReplyImg(replyImg);
        }
        userReport.setReplyTime(new Date());
        userReport.setReportStatus(2);
        return userReportService.updateReport(userReport);
    }

    @PutMapping("/commit")
    @ApiOperation(value = "用户提交审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportId", value = "主键id", dataType = "int", required = true),
            @ApiImplicitParam(name = "refuteReason", value = "回复原因", dataType = "string", required = true),
    })
    public ServerResponse commitReport(Integer reportId,String refuteReason){
        UserReport userReport = new UserReport();
        userReport.setReportId(reportId);
        userReport.setRefuteReason(refuteReason);
        userReport.setRefuteTime(new Date());
        userReport.setReportStatus(3);
        return userReportService.updateReport(userReport);
    }

    @PutMapping("/end")
    @ApiOperation(value = "用户结束本次举报信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportId", value = "主键id", dataType = "int", required = true),
    })
    public ServerResponse commitReport(Integer reportId){
        UserReport userReport = new UserReport();
        userReport.setReportId(reportId);
        userReport.setIsEnd(2);
        return userReportService.updateReport(userReport);
    }
    @PutMapping("/admin/audit")
    @ApiOperation(value = "后台审核举报结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportId", value = "主键id", dataType = "int", required = true),
            @ApiImplicitParam(name = "auditReason", value = "审核原因", dataType = "string", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "审核结果（4.通过，5.拒绝）", dataType = "int", required = true),
    })
    public ServerResponse auditReport(Integer reportId,String auditReason,Integer reportStatus){
        UserReport userReport = new UserReport();
        userReport.setReportId(reportId);
        userReport.setAuditReason(auditReason);
        userReport.setReportStatus(reportStatus);
        userReport.setAuditTime(new Date());
        userReport.setIsEnd(2);
        return userReportService.updateAudit(userReport);
    }

    @GetMapping("/jobDetails")
    @ApiOperation(value = "查看任务详情（在待审核列表的任务标题上加上查询任务详情连接）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "用户提交任务主键id", dataType = "int", required = true),
    })
    public ServerResponse selectJobDetails(Integer taskId){
        return userReportService.selectJobDetails(taskId);
    }
}