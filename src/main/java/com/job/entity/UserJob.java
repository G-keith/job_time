package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/5
 */
@Data
public class UserJob {

    @ApiModelProperty(value = "主键id")
    private Integer taskId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "任务id")
    private Integer jobId;

    @ApiModelProperty(value = "状态（1.已报名；2.已提交；3.审核通过；4.审核拒绝；5.已过期）")
    private Integer status;

    @ApiModelProperty(value = "截止提交时间")
    private Date endTime;

    @ApiModelProperty(value = "提交时间")
    private Integer commitTime;

    @ApiModelProperty(value = "不通过理由")
    private Integer refuseReason;
}
