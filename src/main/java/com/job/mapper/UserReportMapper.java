package com.job.mapper;

import com.job.entity.UserReport;
import com.job.entity.vo.JobVo;
import com.job.entity.vo.UserReportVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单
 *
 * @author keith
 * @version 1.0
 * @date 2019-11-18
 */
@Repository
public interface UserReportMapper {
    int deleteByPrimaryKey(Integer reportId);

    int insertSelective(UserReport record);

    UserReport selectByPrimaryKey(Integer reportId);

    int updateByPrimaryKeySelective(UserReport record);

    /**
     * 查询用户没有审核的任务
     * @param reportStatus
     * @param userId
     * @return
     */
    List<UserReportVo> findAll(@Param("reportStatus") Integer reportStatus,@Param("userId") Integer userId);

    /**
     * 查询用户已经审核的任务
     * @param userId
     * @param userId
     * @return
     */
    List<UserReportVo> findUserAudit(Integer userId);

    /**
     * 查询用户举报的任务
     * @param reportStatus
     * @param userId
     * @return
     */
    List<UserReportVo> findRewardAll(@Param("reportStatus") Integer reportStatus,@Param("userId") Integer userId);

    /**
     * 查询已经审核的任务
     * @param userId
     * @param userId
     * @return
     */
    List<UserReportVo> findRewardAudit(Integer userId);

    /**
     * 查询已经审核的任务
     * @return
     */
    List<UserReportVo> findAudit();

    /**
     * 查询待审核的任务
     * @return
     */
    List<UserReportVo> findWillAudit();

    /**
     * 查询用户详情
     * @param taskId
     * @return
     */
    JobVo selectJobDetails(Integer taskId);
}