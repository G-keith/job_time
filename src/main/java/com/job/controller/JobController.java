package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.Job;
import com.job.service.JobService;
import io.swagger.annotations.Api;
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
@Api(tags = "手机端任务管理接口")
@RequestMapping(value = "/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

}
