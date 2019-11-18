package com.job.mapper;

import com.job.entity.UserOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrderMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(UserOrder record);

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(UserOrder record);

    int updateByPrimaryKey(UserOrder record);

    /**
     * 根据订单号更新订单状态
     * @param userOrder
     * @return
     */
    int  updateByOrderNum(UserOrder userOrder);

    /**
     * 根据订单号查询订单信息
     * @param OrderNum
     * @return
     */
    UserOrder selectByOrderNum(String OrderNum);
}