package com.job.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/5
 */
@Data
public class JobCommitVo {

    @ApiModelProperty(value = "主键id")
    private Integer jobId;

    @ApiModelProperty(value = "发布人id")
    private Integer userId;

    @ApiModelProperty(value = "提交信息")
    private String commitInfo;

    @ApiModelProperty(value = "任务验证步骤")
    private List<JobStepDto> jobStepDtoList;
}
