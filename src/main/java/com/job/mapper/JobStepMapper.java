package com.job.mapper;

import com.job.entity.JobStep;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019-10-29
 */
@Repository
public interface JobStepMapper {
    int deleteByPrimaryKey(Integer stepId);

    int insertSelective(JobStep record);

    JobStep selectByPrimaryKey(Integer stepId);

    int updateByPrimaryKeySelective(JobStep record);

    /**
     * 批量插入任务步骤
     * @param jobStepList 任务步骤集合
     */
    int insertBatch(@Param("jobStepList") List<JobStep> jobStepList);
}