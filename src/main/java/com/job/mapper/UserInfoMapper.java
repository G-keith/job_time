package com.job.mapper;

import com.job.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户信息数据持久层
 * @author keith
 * @version 1.0
 * @date 2019-10-28
 */
@Repository
public interface UserInfoMapper {

    /**
     * 查询手机号是否存在
     * @param phone 手机号码
     * @return 0没有
     */
    UserInfo findByPhone(String phone);

    /**
     * 查询用户信息
     * @param userId 主键id
     * @return 0没有
     */
    UserInfo findByUserId(Integer userId);

    /**
     * 插入用户信息
     * @param userInfo 用户信息
     * @return 0失败
     */
    int insertPhone(UserInfo userInfo);

    /**
     * 插入验证码到数据库中去
     * @param phone 手机号
     * @param code 验证码
     * @return 0
     */
    int insertCode(@Param("phone")String phone,@Param("code")String code);

    /**
     * 通过手机号查询验证码
     * @param phone 手机号码
     * @return 验证码
     */
    String findCodeByPhone(String phone);

    /**
     * 更新验证码为已使用
     * @param phone 手机号
     * @return 0失败，1成功
     */
    int  updateCode(String phone);

    /**
     * uid是否存在
     * @param UID uid
     * @return 0不存在
     */
    int UidIsExist(String UID);
}