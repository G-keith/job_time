package com.job.mapper;

import com.job.entity.JobType;
import com.job.entity.UserJob;
import com.job.entity.vo.JobDto;
import com.job.entity.vo.JobListVo;
import com.job.entity.vo.JobStepDto;
import com.job.entity.vo.JobVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Repository
public interface HallMapper {

    /**
     * 查询所有任务分类
     * @return 所有任务分类
     */
    List<JobType> selectAll();

    /**
     * 查询任务列表详情
     * @param jobDto 查询条件
     * @return 任务列表
     */
    List<JobListVo> findList(JobDto jobDto);

    /**
     * 查询任务列表详情
     * @param jobDto 查询条件
     * @return 任务列表
     */
    List<JobListVo> findNew(JobDto jobDto);

    /**
     * 插入用户浏览记录
     * @param userId 用户id
     * @param jobId 任务id
     * @return 0失败，1成功
     */
    int insertFootprint(@Param("userId") Integer userId,@Param("jobId") Integer jobId);

    /**
     * 查询任务详情
     * @param jobId 任务id
     * @param userId 用户id
     * @return 任务详情
     */
    JobVo selectJobDetails(@Param("userId") Integer userId,@Param("jobId") Integer jobId);

    /**
     * 查询任务详情
     * @param jobId 任务id
     * @return 任务详情
     */
    JobVo selectJob(@Param("jobId") Integer jobId);

    /**
     * 插入用户报名信息
     * @param userJob 用户报名信息
     * @return 0失败
     */
    int userSignUp(UserJob userJob);

    /**
     * 查询用户有没有做过任务
     * @param userId 用户id
     * @param jobId 任务id
     * @param type 2,当天
     * @return 用户任务关系
     */
    Integer selectUserJob(@Param("userId") Integer userId,@Param("jobId") Integer jobId,@Param("type") Integer type);


    /**
     * 更新用户报名次数
     * @param jobId 任务id
     * @return 0失败
     */
    int updateJob(Integer jobId);

    /**
     * 更新审核中次数
     * @param jobId 任务id
     * @return 0失败
     */
    int updateCommitNum(Integer jobId);

    /**
     * 更新用户任务关系表
     * @param userId 用户id
     * @param jobId 任务id
     * @param commitInfo 提交信息
     * @return 0失败
     */
    int updateUserJob(@Param("userId") Integer userId,@Param("jobId") Integer jobId,@Param("commitInfo") String commitInfo);

    /**
     * 查询任务主键id
     * @param userId 用户id
     * @param jobId 任务id
     * @return 主键id
     */
    int selectId(@Param("userId") Integer userId,@Param("jobId") Integer jobId);

    /**
     * 查询用户对账户提交次数
     * @param userId 用户id
     * @param jobId 任务id
     * @return 提交次数
     */
    int  userCommit(@Param("userId") Integer userId,@Param("jobId") Integer jobId);

    /**
     * 插入提交任务信息
     * @param jobStepList 任务步骤
     * @param taskId 任务主键id
     * @return 0失败
     */
    int insertBatch(@Param("jobStepList") List<JobStepDto> jobStepList, @Param("taskId") Integer taskId);
}
