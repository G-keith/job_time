package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务步骤实体
 * @author keith
 * @version 1.0
 * @date 2019-10-29
 */
@Data
public class JobStep {

    @ApiModelProperty(value = "主键id")
    private Integer stepId;

    @ApiModelProperty(value = "步骤类型 1.图片，2网址")
    private Integer stepType;

    @ApiModelProperty(value = "任务id")
    private Integer jobId;

    @ApiModelProperty(value = "步骤说明")
    private String introduce;

    @ApiModelProperty(value = "步骤排序")
    private Integer sort;

    @ApiModelProperty(value = "是否需要验证图（1，不需要，2需要）")
    private Integer checkPicture;

    @ApiModelProperty(value = "图片地址")
    private String picture;

    @ApiModelProperty(value = "网址")
    private String website;

    @ApiModelProperty(value = "用户提交的图片")
    private String commitPicture;
}