package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Data
public class HomePage {

    @ApiModelProperty(value = "主键id")
    private Integer imgId;

    @ApiModelProperty(value = "图片路径")
    private String img;

    @ApiModelProperty(value = "图片详情")
    private String details;
}
