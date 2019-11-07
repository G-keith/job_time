package com.job.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Data
public class JobDto {

    @ApiModelProperty(value = "0：默认;1.新人；2，简单；3.高价，4.最新")
    private Integer label;

    @ApiModelProperty(value = "关键词")
    private String keyWord;

    @ApiModelProperty(value = "任务类型id")
    private Integer typeId;

}
