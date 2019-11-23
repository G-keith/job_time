package com.job.mapper;

import com.job.entity.UserCashOut;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/14
 */
@Repository
public interface UserCashOutMapper {

    /**
     * 插入用户提现申请信息
     * @param record
     * @return
     */
    int insertSelective(UserCashOut record);

    /**
     * 根据主键查询提现申请信息
     * @param cashOutId
     * @return
     */
    UserCashOut selectByPrimaryKey(Integer cashOutId);

    /**
     * 更新提现申请信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserCashOut record);

    /**
     * 查询所有提现申请信息
     * @param userCashOut 提现申请信息
     * @return
     */
    List<UserCashOut> findAll(UserCashOut userCashOut);

    /**
     * 查询所有提现申请信息
     * @param userId 用户id
     * @return
     */
    List<UserCashOut> findByUserId(Integer userId);

    /**
     * 查询用户当天有没有提现过
     * @param userId
     * @return
     */
    int countNow(Integer userId);

}