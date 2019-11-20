package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Data
public class SysRule {

    @ApiModelProperty(value = "主键id")
    private Integer ruleId;
    @ApiModelProperty(value = "规则类型（1.发布规则；2接单规则；3.免责声明；4.保证金说明；5.充值说明，6.平台介绍）")
    private Integer ruleType;
    @ApiModelProperty(value = "备注")
    private String remarks;
}
