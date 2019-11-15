package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019-10-29
 */
@Data
public class JobType {

    @ApiModelProperty(value = "主键id")
    private Integer typeId;

    @ApiModelProperty(value = "分类名称")
    private String typeName;

    @ApiModelProperty(value = "描述")
    private String introduce;
}