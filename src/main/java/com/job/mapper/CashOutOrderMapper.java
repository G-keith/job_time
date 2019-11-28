package com.job.mapper;

import com.job.entity.CashOutOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface CashOutOrderMapper {
    int deleteByPrimaryKey(Integer cashOutId);

    int insertSelective(CashOutOrder record);

    CashOutOrder selectByPrimaryKey(Integer cashOutId);

    int updateByPrimaryKeySelective(CashOutOrder record);

}