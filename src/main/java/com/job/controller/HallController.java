package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.vo.JobCommitVo;
import com.job.entity.vo.JobDto;
import com.job.service.HallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Slf4j
@RestController
@Api(tags = "大厅接口")
@RequestMapping(value = "/hall")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping("/type")
    @ApiOperation(value = "查询所有任务类别")
    public ServerResponse selectType(){
        return hallService.selectAll();
    }

    @GetMapping("/job")
    @ApiOperation(value = "查询所有任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "label", value = "标签 0：默认;1.新人；2，简单；3.高价，4.最新", dataType = "int",required = true),
            @ApiImplicitParam(name = "keyWord", value = "关键词", dataType = "string"),
            @ApiImplicitParam(name = "typeId", value = "任务类型id", dataType = "int"),
    })
    public ServerResponse jobList(Integer pageNo,Integer pageSize,Integer label,String keyWord,Integer typeId){
        JobDto jobDto=new JobDto();
        jobDto.setLabel(label);
        if(keyWord!=null){
            jobDto.setKeyWord(keyWord);
        }
        if(typeId!=null){
            jobDto.setTypeId(typeId);
        }
        return hallService.selectJobList(jobDto,pageNo,pageSize);
    }

    @GetMapping("/jobDetails")
    @ApiOperation(value = "查看任务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobId", value = "任务id", dataType = "int",required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int"),
    })
    public ServerResponse selectJobDetails(Integer jobId,Integer userId){
        return hallService.selectJobDetails(jobId, userId);
    }
    //报名，提交验证图

    @GetMapping("/signUp")
    @ApiOperation(value = "报名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobId", value = "任务id", dataType = "int",required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int",required = true),
    })
    public ServerResponse signUp(Integer userId,Integer jobId){
        return hallService.signUp(userId, jobId);
    }

    @GetMapping("/submit")
    @ApiOperation(value = "提交任务")
    public ServerResponse submit(@RequestBody JobCommitVo jobCommitVo){
        return hallService.submit(jobCommitVo);
    }
}
