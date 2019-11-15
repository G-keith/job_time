package com.job.mapper;

import com.job.entity.UserInfo;
import com.job.entity.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/12
 */
@Repository
public interface UserFollowMapper {

    /**
     * 查询用户是否关注对方
     * @param userId 用户id
     * @param followId 被关注者id
     * @return
     */
    int isFollow(@Param("userId") Integer userId, @Param("followId") Integer followId);

    /**
     * 插入用户关注列表
     * @param userId 用户id
     * @param followId 被关注者id
     * @return
     */
    int insertFollow(@Param("userId") Integer userId,@Param("followId") Integer followId);

    /**
     * 删除用户关注
     * @param userId 用户id
     * @param followId 被关注者id
     * @return
     */
    int deleteFollow(@Param("userId") Integer userId,@Param("followId") Integer followId);

    /**
     * 查询关注列表
     * @param userId 用户id
     * @return
     */
    List<UserInfoVo> findAll(Integer userId);
}
