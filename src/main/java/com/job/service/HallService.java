package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.DateUtils;
import com.job.entity.Job;
import com.job.entity.UserInfo;
import com.job.entity.UserJob;
import com.job.entity.vo.JobCommitVo;
import com.job.entity.vo.JobDto;
import com.job.entity.vo.JobListVo;
import com.job.entity.vo.JobVo;
import com.job.mapper.HallMapper;
import com.job.mapper.JobMapper;
import com.job.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HallService {

    private final HallMapper hallMapper;

    private final UserInfoMapper userInfoMapper;

    private final JobMapper jobMapper;

    public HallService(HallMapper hallMapper, UserInfoMapper userInfoMapper, JobMapper jobMapper) {
        this.hallMapper = hallMapper;
        this.userInfoMapper = userInfoMapper;
        this.jobMapper = jobMapper;
    }

    /**
     * 查询所有任务类型
     *
     * @return 所有任务类型
     */
    public ServerResponse selectAll() {
        return ServerResponse.createBySuccess(hallMapper.selectAll());
    }


    /**
     * 查询大厅任务列表
     *
     * @param jobDto   查询条件
     * @param pageNo   第几页
     * @param pageSize 每页几条
     * @return 任务列表
     */
    public ServerResponse<PageVO<JobListVo>> selectJobList(JobDto jobDto, Integer pageNo, Integer pageSize) {
        Page<JobListVo> page = PageHelper.startPage(pageNo, pageSize);
        if (jobDto.getLabel() == 4) {
            hallMapper.findNew(jobDto);
            return ServerResponse.createBySuccess(PageVO.build(page));
        } else {
            hallMapper.findList(jobDto);
            return ServerResponse.createBySuccess(PageVO.build(page));
        }
    }

    /**
     * 查询任务详情
     *
     * @param jobId  任务id
     * @param userId 用户id
     * @return 任务详情
     */
    public ServerResponse selectJobDetails(Integer jobId, Integer userId) {
        JobVo jobVo;
        if(userId!=null){
            jobVo = hallMapper.selectJobDetails(userId, jobId);
        }else{
            jobVo=hallMapper.selectJob(jobId);
        }
        if (userId != null) {
            hallMapper.insertFootprint(userId, jobId);
            return ServerResponse.createBySuccess(jobVo);
        }else{
            return ServerResponse.createBySuccess(jobVo);
        }
    }

    /**
     * 用户报名
     *
     * @param userId 用户id
     * @param jobId  任务id
     * @return 0成功，1失败
     */
    public ServerResponse signUp(Integer userId, Integer jobId) {
        UserInfo userInfo=userInfoMapper.findByUserId(userId);
        if(userInfo.getStatus()==1){
            return ServerResponse.createByErrorMessage("用户为黑名单，不能参与活动");
        }
        Job job=jobMapper.selectJob(jobId);
        // 判断用户当天有没有做过，或者以前有没有做过
        Integer res=hallMapper.selectUserJob(userId,jobId,job.getJobRate());
        if(res>0){
            return ServerResponse.createByErrorMessage("用户参与次数达到上限");
        }
        if (job.getSurplusNum() < 0) {
            return ServerResponse.createByErrorMessage("任务参与人数达到上限");
        } else {
            UserJob userJob=new UserJob();
            userJob.setUserId(userId);
            userJob.setJobId(jobId);
            userJob.setEndTime(DateUtils.getDate_add(new Date(),job.getSubmissionTime(),4));
            userJob.setEnrollTime(new Date());
            int result = hallMapper.userSignUp(userJob);
            if (result > 0) {
                //减去任务报名次数
                hallMapper.updateJob(jobId);
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByErrorMessage("报名失败");
            }
        }
    }

    /**
     * 提交验证图
     * @param jobCommitVo 验证信息
     * @return 0失败
     */
    public ServerResponse submit(JobCommitVo jobCommitVo){
        if(hallMapper.userCommit(jobCommitVo.getUserId(),jobCommitVo.getJobId())>3){
            return ServerResponse.createByErrorCodeMessage(2,"您提交次数太多");
        }else{
            //更新任务表
            hallMapper.updateCommitNum(jobCommitVo.getJobId());
            //更新用户任务关系表
            hallMapper.updateUserJob(jobCommitVo.getUserId(),jobCommitVo.getJobId());
            //插入提交任务
            if(jobCommitVo.getJobStepDtoList().size()>0){
                int result=hallMapper.insertBatch(jobCommitVo.getJobStepDtoList(),hallMapper.selectId(jobCommitVo.getUserId(),jobCommitVo.getJobId()));
                if(result>0){
                    return ServerResponse.createBySuccess();
                }else{
                    return ServerResponse.createByErrorMessage("提交失败");
                }
            }else{
                return ServerResponse.createBySuccess();
            }
        }
    }
}
