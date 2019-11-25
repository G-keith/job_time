package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserInfo;
import com.job.entity.UserOpinion;
import com.job.mapper.UserOpinionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserOpinionService {
    @Autowired
    private UserOpinionMapper userOpinionMapper;

    /**
     * 插入用户反馈
     * @param userOpinion
     * @return
     */
    public ServerResponse insertSelective(UserOpinion userOpinion){
        int result=userOpinionMapper.insertSelective(userOpinion);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 更新反馈
     * @param userOpinion
     * @return
     */
    public ServerResponse updateByPrimaryKeySelective(UserOpinion userOpinion){
        int result=userOpinionMapper.updateByPrimaryKeySelective(userOpinion);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询所有用户反馈
     * @param userId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findAll(Integer userId,Integer status,Integer pageNo, Integer pageSize){
        Page<UserOpinion> page = PageHelper.startPage(pageNo, pageSize);
        userOpinionMapper.findAll(userId, status);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }
}