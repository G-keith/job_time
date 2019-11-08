package com.job.mapper;

import com.job.entity.ServiceFee;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Repository
public interface ServiceFeeMapper {

    /**
     * 查询所有服务费类型
     * @return
     */
    List<ServiceFee> findAll();

    /**
     * 更新服务费信息
     * @param  serviceFee
     * @return
     */
    int updateServiceFee(ServiceFee serviceFee);

}
