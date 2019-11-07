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

    /**
     * 插入任务步骤
     *
     * @param job 任务信息
     * @return 0成功，1失败
     */
    public ServerResponse insertJob(Job job) {
        //判断账户保证金以及 账户余额是否充足
        UserMoney userMoney = jobMapper.selectMoney(job.getUserId());
        BigDecimal bond = sysBondMapper.findBond();
        if (userMoney.getBond().compareTo(bond) < 0) {
            return ServerResponse.createByErrorMessage("保证金不能小于" + bond + "元");
        }
        if (userMoney.getBalance().compareTo(job.getJobPrice()) < 0) {
            return ServerResponse.createByErrorMessage("账户余额不足");
        }
        int result = jobMapper.insertSelective(job);
        if (result > 0) {
            //插入任务步骤
            job.getJobStepList().forEach(e -> e.setJobId(job.getJobId()));
            int res = jobStepMapper.insertBatch(job.getJobStepList());
            //插入任务和类型关系表
            jobMapper.insertJobType(job.getJobTypeList(), job.getJobId());
            if (res > 0) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByError();
            }
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询所有任务类型
     *
     * @return 任务类型
     */
    public ServerResponse selectJobType() {
        return ServerResponse.createBySuccess(jobMapper.selectJobType());
    }
}
