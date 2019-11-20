package com.job.entity.vo;

import com.job.entity.SysRuleDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/20
 */
@Data
public class SysRuleDetailsVo {

    @ApiModelProperty(value = "规则主键id")
    private Integer ruleId;

    @ApiModelProperty(value = "规则列表")
    private List<SysRuleDetails> sysRuleDetailsList;
}
