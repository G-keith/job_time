package com.job.mapper;

import com.job.entity.Job;
import com.job.entity.UserJob;
import com.job.entity.vo.JobListVo;
import com.job.entity.vo.JobVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/10
 */
@Repository
public interface UserJobMapper {

    /**
     * 查询用户所有提交的任务
     * @param userId 用户id
     * @param status 任务状态
     * @return
     */
    List<JobListVo> findByUserId(@Param("userId") Integer userId, @Param("status") Integer status);

    /**
     * 更新用户提交任务信息
     * @param taskId
     * @param status
     * @param refuseReason
     * @return
     */
    int updateUserJob(@Param("taskId") Integer taskId,@Param("status") Integer status,@Param("refuseReason") String refuseReason);

    /**
     * 查询用户任务完成数
     * @param userId
     * @return
     */
    int selectJobFinishNum(@Param("userId") Integer userId);

    UserJob findById(Integer taskId);
}
