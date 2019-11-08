package com.job.mapper;

import com.job.entity.SysRule;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Repository
public interface SysRuleMapper {

    /**
     * 插入规则描述
     * @param sysRule
     * @return
     */
    int insertRule(SysRule sysRule);

    /**
     * 根据类型查询规则
     * @param ruleType
     * @return
     */
    SysRule selectByType(Integer ruleType);

    /**
     * 查询所有规则
     * @return
     */
    List<SysRule> findAll();

    /**
     * 更新规则
     * @param sysRule
     * @return
     */
    int updateRule(SysRule sysRule);
}
