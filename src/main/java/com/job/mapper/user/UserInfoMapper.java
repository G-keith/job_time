package com.job.mapper.user;

import com.job.entity.user.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * 用户信息数据持久层
 * @author keith
 * @version 1.0
 * @date 2019-10-28
 */
@Repository
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}