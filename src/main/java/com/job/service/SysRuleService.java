package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.SysRule;
import com.job.entity.vo.SysRuleDetailsVo;
import com.job.mapper.SysRuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 查询规则详细信息
     * @return
     */
    public ServerResponse findDetails(Integer ruleId){
        return ServerResponse.createBySuccess(sysRuleMapper.findDetails(ruleId));
    }

    /**
     * 更新规则列表信息
     * @param sysRuleDetailsVo
     * @return
     */
    public ServerResponse updateRule(SysRuleDetailsVo sysRuleDetailsVo){
        sysRuleMapper.deleteRuleDetails(sysRuleDetailsVo.getRuleId());
        if(sysRuleDetailsVo.getSysRuleDetailsList().size()>0){
            int result=sysRuleMapper.insertRuleDetails(sysRuleDetailsVo.getRuleId(),sysRuleDetailsVo.getSysRuleDetailsList());
            if(result>0){
                return ServerResponse.createBySuccess();
            }else{
                return ServerResponse.createByError();
            }
        }else{
            return ServerResponse.createBySuccess();
        }
    }


}
