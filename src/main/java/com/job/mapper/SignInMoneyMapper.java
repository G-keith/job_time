package com.job.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Repository
public interface SignInMoneyMapper {

    /**
     * 查询签到或者邀请金额
     * @return
     */
    Map<String,Object> findAll();

    /**
     * 更新签到或者邀请金额
     * @param money
     * @param inviteMoney
     * @return
     */
    int updateSignInMoney(@Param("money") BigDecimal money, @Param("inviteMoney") BigDecimal inviteMoney);
}
