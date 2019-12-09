package com.job.mapper;

import com.job.entity.UserOpinion;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/23
 */
@Repository
public interface UserOpinionMapper {
    /**
     * 插入用户意见
     * @param record
     * @return
     */
    int insertSelective(UserOpinion record);

    /**
     * 更新用户意见信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserOpinion record);

    /**
     * 查询用户反馈列表
     * @param phone
     * @param status
     * @param nickName
     * @return
     */
    List<UserOpinion> findAll(@Param("phone") String phone,@Param("nickName") String nickName,@Param("userId") Integer userId ,@Param("status")Integer status);
}