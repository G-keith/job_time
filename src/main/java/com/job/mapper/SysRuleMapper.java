package com.job.mapper;

import com.job.entity.SysRule;
import com.job.entity.SysRuleDetails;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Repository
public interface SysRuleMapper {

    /**
     * 查询所有规则
     * @return
     */
    List<SysRule> findAll();

    /**
     * 查询规则详细信息
     * @param ruleId
     * @return
     */
    List<Map<String,Object>> findDetails(Integer ruleId);

    /**
     * 删除规则详细信息
     * @param ruleId 主键id
     * @return
     */
    int deleteRuleDetails(Integer ruleId);

    /**
     * 插入规则详细信息
     * @param sysRuleDetailsList
     * @param ruleId
     * @return
     */
    int insertRuleDetails(@Param("ruleId") Integer ruleId,@Param("sysRuleDetailsList") List<SysRuleDetails> sysRuleDetailsList);
}
