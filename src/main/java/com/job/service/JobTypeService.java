package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.JobType;
import com.job.mapper.JobTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JobTypeService {

    private final JobTypeMapper jobTypeMapper;

    public JobTypeService(JobTypeMapper jobTypeMapper) {
        this.jobTypeMapper = jobTypeMapper;
    }

    /**
     * 查询所有的任务类型
     * @param pageNo 第几页
     * @param pageSize 每页几条
     * @return 所有的任务类型
     */
    public ServerResponse findAll(Integer pageNo, Integer pageSize){
        Page<JobType> page = PageHelper.startPage(pageNo, pageSize);
        jobTypeMapper.findAll();
        return ServerResponse.createBySuccess(PageVO.build(page));
    }
    /**
     * 添加任务类型
     * @param jobType 任务类型
     * @return 0成功，1失败
     */
    public ServerResponse add(JobType jobType){
        int result=jobTypeMapper.insertSelective(jobType);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 更新任务类型
     * @param jobType 任务类型
     * @return 0成功，1失败
     */
    public ServerResponse edit(JobType jobType){
        int result=jobTypeMapper.updateByPrimaryKeySelective(jobType);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 删除
     * @param typeId 主键id
     * @return 0成功，1失败
     */
    public ServerResponse delete(Integer typeId){
        int result=jobTypeMapper.deleteByPrimaryKey(typeId);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }
}
