package com.job.mapper;

import com.job.entity.Job;
import org.springframework.stereotype.Repository;

/**
 * @author keith
 * @version 1.0
 * @date 2019-10-29
 */
@Repository
public interface JobMapper {
    int deleteByPrimaryKey(Integer jobId);

    /**
     * 插入任务信息
     * @param record 任务信息
     * @return 0失败，1成功
     */
    int insertSelective(Job record);

    int updateByPrimaryKeySelective(Job record);

    /**
     * 查询任务信息
     * @param jobId 任务id
     * @return 任务信息
     */
    Job selectJob(Integer jobId);



}