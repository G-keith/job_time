package com.job.mapper;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.Job;
import com.job.entity.JobStep;
import com.job.entity.ServiceFee;
import com.job.entity.vo.JobVo;
import com.job.entity.vo.UserJobVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    /**
     * 更新任务信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Job record);

    /**
     * 查询任务信息
     * @param jobId 任务id
     * @return 任务信息
     */
    Job selectJob(Integer jobId);

    /**
     * 查询所有待审核任务
     * @return
     */
    List<JobVo> findAll(@Param("jobTitle") String jobTitle,@Param("jobSource") String jobSource,@Param("typeId") Integer typeId);

    /**
     * 查询任务步骤详情
     * @return
     */
    List<JobStep> findAllStep(Integer jobId);

    /**
     * 更新任务审核结果
     * @param jobId 任务id
     * @param auditStatus 审核结果
     * @param refuseReason 拒绝原因
     * @param isRecommend
     * @return
     */
    int updateJob(@Param("jobId") Integer jobId,@Param("auditStatus") Integer auditStatus,@Param("refuseReason") String refuseReason,@Param("isRecommend")Integer isRecommend);

    /**
     * 查询用户足迹
     * @param userId 用户id
     * @return
     */
    List<Job> findFootprint(Integer userId);

    /**
     * 查询用户所发布的任务
     * @param userId 用户id
     * @param auditStatus 审核状态
     * @return
     */
    List<JobVo> findRelease(Integer userId,Integer auditStatus);

    /**
     * 查询用户结束的任务
     * @param userId 用户id
     * @return
     */
    List<JobVo> findEndRelease(Integer userId);

    /**
     * 查询需要审核任务列表
     * @param userId
     * @return
     */
    List<JobVo> findWillAudit(Integer userId);

    /**
     * 查询任务待审核列表
     * @param status 状态
     * @param userId 用户id
     * @return
     */
    List<UserJobVo> findUserJob(@Param("status") Integer status,@Param("userId") Integer userId);

    /**
     * 查询验证图
     * @param taskId
     * @return
     */
    List<String> findCheckPicture(Integer taskId);

    /**
     * 查询发布任务时的服务费
     * @return
     */
    ServiceFee findFee();

    /**
     * 查询奖励提现时的服务费
     * @return
     */
    ServiceFee findCashOut();

    /**
     * 查询充值时的服务费
     * @return
     */
    ServiceFee findRecharge();

    /**
     * 查询正在进行中的任务数量
     * @param userId
     * @return
     */
    int countIn(Integer userId);

}