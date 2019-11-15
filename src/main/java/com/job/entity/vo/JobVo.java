package com.job.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.job.entity.JobStep;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 任务实体
 * @author keith
 * @version 1.0
 * @date 2019-10-29
 */
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class JobVo {

    @ApiModelProperty(value = "主键id")
    private Integer jobId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value = "用户推广码")
    private Integer UID;

    @ApiModelProperty(value = "任务标题")
    private String jobTitle;

    @ApiModelProperty(value = "项目名称")
    private String jobSource;

    @ApiModelProperty(value = "悬赏价格")
    private BigDecimal releasePrice;

    @ApiModelProperty(value = "任务数量")
    private Integer jobNum;

    @ApiModelProperty(value = "剩余数量")
    private Integer surplusNum;

    @ApiModelProperty(value = "任务标签")
    private String typeName;

    @ApiModelProperty(value = "几小时内提交")
    private Integer submissionTime;

    @ApiModelProperty(value = "描述")
    private String introduce;

    @ApiModelProperty(value = "任务步骤详情")
    List<JobStep> jobStepList;

    @ApiModelProperty(value = "状态（0.没有报名；,1.已报名；2.已提交；3.审核通过；4.审核拒绝）")
    private Integer status;

    @ApiModelProperty(value = "待审核任务条数")
    private Integer num;

    @ApiModelProperty(value = "审核状态（1.未提交；2.审核中，3审核通过；4；审核拒绝）")
    private Integer auditStatus;
}