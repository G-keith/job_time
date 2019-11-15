package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserInfo;
import com.job.entity.vo.JobListVo;
import com.job.entity.vo.UserInfoVo;
import com.job.mapper.UserFollowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserFollowService {

    @Autowired
    private UserFollowMapper userFollowMapper;

    /**
     * 查询用户是否关注过
     * @param userId
     * @param followId
     * @return
     */
    public ServerResponse isFollow(Integer userId,Integer followId){
        int result = userFollowMapper.isFollow(userId,followId);
        if (result == 1) {
            return ServerResponse.createBySuccess("已经关注过", 1);
        } else {
            return ServerResponse.createBySuccess("没有关注过", 2);
        }
    }

    /**
     * 插入用户关注列表
     * @param userId 用户id
     * @param followId 被关注者id
     * @return
     */
    public ServerResponse insertFollow(Integer userId,Integer followId){
        int result = userFollowMapper.insertFollow(userId,followId);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 删除用户关注
     * @param userId 用户id
     * @param followId 被关注者id
     * @return
     */
    public ServerResponse deleteFollow(Integer userId,Integer followId){
        int result = userFollowMapper.deleteFollow(userId,followId);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询关注列表
     * @param userId 用户id
     * @return
     */
    public ServerResponse findAll(Integer userId,Integer pageNo,Integer pageSize){
        Page<UserInfoVo> page = PageHelper.startPage(pageNo, pageSize);
        userFollowMapper.findAll(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }
}
