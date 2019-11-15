package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.vo.JobListVo;
import com.job.entity.vo.JobVo;
import com.job.mapper.UserJobMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserJobService {

    @Autowired
    private UserJobMapper userJobMapper;

    /**
     * 查询用户所有提交的任务
     * @param userId
     * @param status
     * @return
     */
    public ServerResponse findByUserId(Integer userId, Integer status,Integer pageNo,Integer pageSize){
        Page<JobListVo> page = PageHelper.startPage(pageNo, pageSize);
        userJobMapper.findByUserId(userId, status);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }
}
