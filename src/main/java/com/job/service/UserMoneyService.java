package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserMoney;
import com.job.mapper.UserMoneyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMoneyService {

    @Autowired
    private UserMoneyMapper userMoneyMapper;

    /**
     * 查询用户账户信息
     * @param userId 用户id
     * @return
     */
    public ServerResponse<UserMoney> findMoney(Integer userId){
        return ServerResponse.createBySuccess(userMoneyMapper.selectById(userId));
    }
}
