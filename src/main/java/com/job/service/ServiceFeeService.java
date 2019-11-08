package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.ServiceFee;
import com.job.mapper.ServiceFeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceFeeService {

    @Autowired
    private ServiceFeeMapper serviceFeeMapper;

    /**
     * 查询所有服务费
     * @return
     */
    public ServerResponse findAll(){
        return ServerResponse.createBySuccess(serviceFeeMapper.findAll());
    }

    /**
     * 更新服务费
     * @param serviceFee
     * @return
     */
    public ServerResponse updateServiceFee(ServiceFee serviceFee){
        int result=serviceFeeMapper.updateServiceFee(serviceFee);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }
}
