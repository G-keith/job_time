package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.Job;
import com.job.entity.UserMoney;
import com.job.mapper.JobMapper;
import com.job.mapper.JobStepMapper;
import com.job.mapper.SysBondMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JobService {

    private final JobMapper jobMapper;

    private final JobStepMapper jobStepMapper;

    private final SysBondMapper sysBondMapper;

    public JobService(JobMapper jobMapper, JobStepMapper jobStepMapper, SysBondMapper sysBondMapper) {
        this.jobMapper = jobMapper;
        this.jobStepMapper = jobStepMapper;
        this.sysBondMapper = sysBondMapper;
    }
}
