package com.job.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
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

    @ApiModelProperty(value = "状态（1.已报名；3.已提交；4.审核通过；5.审核拒绝；2.已过期）")
    private Integer status;

    @ApiModelProperty(value = "截止提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "报名时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date enrollTime;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date commitTime;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date auditTime;

    @ApiModelProperty(value = "不通过理由")
    private String refuseReason;

    /**
     * 任务发布价格
     */
    private BigDecimal releasePrice;
}
