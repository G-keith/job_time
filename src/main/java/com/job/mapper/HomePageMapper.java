package com.job.mapper;

import com.job.entity.HomePage;
import com.job.entity.UserMoney;
import com.job.entity.vo.JobDto;
import com.job.entity.vo.JobListVo;
import com.job.entity.vo.JobVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Repository
public interface HomePageMapper {

    /**
     * 查询所有轮播图
     * @return 0失败
     */
    List<HomePage> findAll();

    /**
     * 查询推荐悬赏
     * @return 推荐悬赏
     */
    List<JobListVo> findRecommend();

    /**
     * 用户当天是否签到过
     * @param userId 用户id
     * @return 0没有，1有
     */
    int isSignIn(Integer userId);

    /**
     * 插入用户签到记录
     * @param userId 用户id
     * @return 0失败，1成功
     */
    int insertSignIn(Integer userId);

    /**
     * 查询用户账户信息
     * @param userId 用户id
     * @return 用户账户信息
     */
    UserMoney selectByUserId(Integer userId);

    /**
     * 查询签到金额
     * @return 签到金额
     */
    Map<String,BigDecimal> selectSignInMoney();

    /**
     * 插入用户账户信息
     * @param userMoney  用户账户信息
     * @return 0失败，1成功
     */
    int insertUserMoney(UserMoney userMoney);
}
