package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.mapper.SignInMoneyMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SignInMoneyService {

    @Autowired
    private SignInMoneyMapper signInMoneyMapper;

    /**
     * 查询所有
     * @return
     */
    public ServerResponse findAll(){
        return ServerResponse.createBySuccess(signInMoneyMapper.findAll());
    }

    /**
     * 更新签到和邀请金额
     * @param money
     * @param inviteMoney
     * @return
     */
    public ServerResponse update( BigDecimal money, BigDecimal inviteMoney){
        int result=signInMoneyMapper.updateSignInMoney(money, inviteMoney);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }
}
