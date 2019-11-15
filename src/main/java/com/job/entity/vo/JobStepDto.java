package com.job.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/5
 */
@Data
public class JobStepDto {

    @ApiModelProperty(value = "步骤排序")
    private Integer sort;

    @ApiModelProperty(value = "验证图")
    private String checkPicture;

}
