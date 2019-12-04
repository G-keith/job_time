package com.job.mapper;

import com.job.entity.CashOutOrder;
import com.job.entity.vo.CashOutOrderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashOutOrderMapper {
    int deleteByPrimaryKey(Integer cashOutId);

    int insertSelective(CashOutOrder record);

    CashOutOrder selectByPrimaryKey(Integer cashOutId);

    int updateByPrimaryKeySelective(CashOutOrder record);

    /**
     * 查询打款记录
     * @param phone
     * @param nickName
     * @return
     */
    List<CashOutOrderVo> findCashOut(@Param("phone") String phone, @Param("nickName") String nickName);

}