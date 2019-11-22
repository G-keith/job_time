package com.job.mapper;

import com.job.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
     *
     * @param phone 手机号码
     * @return 0没有
     */
    UserInfo findByPhone(String phone);

    /**
     * 通过openId查询是否存在
     *
     * @param openId openId
     * @return 0没有
     */
    UserInfo findByOpenId(String openId);

    /**
     * 查询用户信息
     *
     * @param userId 主键id
     * @return 0没有
     */
    UserInfo findByUserId(Integer userId);

    /**
     * 查询用户信息
     *
     * @param UID UID
     * @return 0没有
     */
    UserInfo findByUId(String UID);

    /**
     * 插入用户信息
     *
     * @param userInfo 用户信息
     * @return 0失败
     */
    int insertPhone(UserInfo userInfo);

    /**
     * 插入验证码到数据库中去
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 0
     */
    int insertCode(@Param("phone") String phone, @Param("code") String code);

    /**
     * 通过手机号查询验证码
     *
     * @param phone 手机号码
     * @return 验证码
     */
    String findCodeByPhone(String phone);

    /**
     * 更新验证码为已使用
     *
     * @param phone 手机号
     * @return 0失败，1成功
     */
    int updateCode(String phone);

    /**
     * uid是否存在
     *
     * @param UID uid
     * @return 0不存在
     */
    int UidIsExist(String UID);

    /**
     * 查询所有用户信息
     *
     * @param phone
     * @param status
     * @return
     */
    List<UserInfo> findAll(@Param("phone") String phone, @Param("status") Integer status);

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return
     */
    int updateByPrimaryKeySelective(UserInfo userInfo);

    /**
     * 修改管理员账号密码
     *
     * @param account
     * @param password
     * @return
     */
    int modifyAdminInfo(@Param("account") String account, @Param("password") String password);

    /**
     * 后台登录
     *
     * @param account
     * @param password
     * @return
     */
    Map<String, Object> loginAdminInfo(@Param("account") String account, @Param("password") String password);

    /**
     * 插入用户信息
     *
     * @param record
     * @return
     */
    int insertSelective(UserInfo record);
}