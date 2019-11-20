package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/20
 */
@Data
public class SysRuleDetails {

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 规则介绍
     */
    @ApiModelProperty(value = "规则介绍")
    private String introduce;
}
