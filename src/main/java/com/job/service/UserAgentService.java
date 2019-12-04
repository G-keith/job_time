package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.mapper.UserAgentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserAgentService {

    @Autowired
    private UserAgentMapper userAgentMapper;

    /**
     * 插入经纪人内容
     * @param content
     * @return
     */
    public ServerResponse insertAgent(String content){
        userAgentMapper.deleteAgent();
        int result=userAgentMapper.insertAgent(content);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询经纪人内容
     * @return
     */
    public ServerResponse findAgent(){
        return ServerResponse.createBySuccess(userAgentMapper.findAgent());
    }
}
