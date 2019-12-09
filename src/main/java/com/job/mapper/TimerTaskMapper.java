package com.job.mapper;

import com.job.entity.UserInfo;
import com.job.entity.UserJob;
import com.job.entity.UserReport;
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
    int updateMember(@Param("userInfoList") List<UserInfo> userInfoList);

    List<UserInfo> findMember();

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

    /**
     * 查询没有结束的举报信息
     * @return
     */
    List<UserReport> findNotEnd();

    /**
     *
     * @param userReports
     * @return
     */
    int updateReport(List<UserReport> userReports);
}
