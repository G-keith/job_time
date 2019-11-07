package com.job.mapper;

import com.job.entity.JobType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019-10-29
 */
@Repository
public interface JobTypeMapper {
    int deleteByPrimaryKey(Integer typeId);

    int insertSelective(JobType record);

    JobType selectByPrimaryKey(Integer typeId);

    int updateByPrimaryKeySelective(JobType record);

    /**
     * 查询所有任务类型
     * @return 任务类型
     */
    List<JobType> findAll();

}