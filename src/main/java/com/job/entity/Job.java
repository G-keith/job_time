package com.job.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
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
public class Job {

    @ApiModelProperty(value = "主键id")
    private Integer jobId;

    @ApiModelProperty(value = "发布人id")
    private Integer userId;

    @ApiModelProperty(value = "类型id")
    private Integer typeId;

    @ApiModelProperty(value = "项目名称")
    private String jobSource;

    @ApiModelProperty(value = "任务标题")
    private String jobTitle;

    @ApiModelProperty(value = "任务描述")
    private String introduce;

    @ApiModelProperty(value = "做单频率（1.只限一次，2.每日一次")
    private Integer jobRate;

    @ApiModelProperty(value = "任务价格")
    private BigDecimal jobPrice;

    @ApiModelProperty(value = "发布价格")
    private BigDecimal releasePrice;

    @ApiModelProperty(value = "任务次数")
    private Integer jobNum;

    @ApiModelProperty(value = "任务结束时间")
    private Date endTime;

    @ApiModelProperty(value = "几小时内提交")
    private Integer submissionTime;

    @ApiModelProperty(value = "报名中数量")
    private Integer signUpNum;

    @ApiModelProperty(value = "审核中数量")
    private Integer commitNum;

    @ApiModelProperty(value = "已完成数量")
    private Integer finishNum;

    @ApiModelProperty(value = "1.进行中 2.已结束，3.暂停")
    private Integer jobStatus;

    @ApiModelProperty(value = "2待审核，3审核通过4；审核拒绝；")
    private Integer auditStatus;

    @ApiModelProperty(value = "是否推荐（1不推荐，2推荐）")
    private Integer isRecommend;

    @ApiModelProperty(value = "1.没有删除，2已删除")
    private Integer isDelete;

    @ApiModelProperty(value = "1.新人；2，简单；3.高价")
    private Integer label;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date releaseTime;

    @ApiModelProperty(value = "审核通过时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date auditTime;

    @ApiModelProperty(value = "拒绝原因")
    private String refuseReason;

    @ApiModelProperty(value = "剩余数量")
    private Integer surplusNum;

    @ApiModelProperty(value = "任务步骤")
    private List<JobStep> stepList;
}