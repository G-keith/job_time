package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.SysRule;
import com.job.mapper.SysRuleMapper;
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
public class SysRuleService {

    @Autowired
    private SysRuleMapper sysRuleMapper;

    /**
     * 查询所有规则信息
     * @return
     */
    public ServerResponse findAll(){
        return ServerResponse.createBySuccess(sysRuleMapper.findAll());
    }

    /**
     * 更新规则信息
     * @param sysRule
     * @return
     */
    public ServerResponse updateRule(SysRule sysRule){
        int result=sysRuleMapper.updateRule(sysRule);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    public ServerResponse insertRule(SysRule sysRule){
        SysRule rule=sysRuleMapper.selectByType(sysRule.getRuleType());
        if(rule!=null){
            return ServerResponse.createByErrorCodeMessage(2,"同类型规则已经添加，不能重复添加");
        }else{
            int result=sysRuleMapper.insertRule(sysRule);
            if(result>0){
                return ServerResponse.createBySuccess();
            }else{
                return ServerResponse.createByError();
            }
        }
    }
}
