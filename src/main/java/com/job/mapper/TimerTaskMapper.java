package com.job.mapper;

import com.job.entity.UserJob;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/20
 */
@Repository
public interface TimerTaskMapper {

    /**
     * 取消会员
     * @return
     */
    int cancelMember();

    /**
     * 任务过期
     * @return
     */
    int  jobExpire();

    /**
     * 任务提交
     * @param day
     * @return
     */
    List<UserJob> jobCommit(Integer day);

    /**
     * 更新任务为已提交
     * @param userJobList
     * @return
     */
    int updateJobCommit(@Param("userJobList") List<UserJob> userJobList);
}
