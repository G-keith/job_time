package com.job.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/12
 */
@Data
public class JobListVo {

    @ApiModelProperty(value = "主键id")
    private Integer taskId;

    @ApiModelProperty(value = "任务id")
    private Integer jobId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "任务标题")
    private String jobTitle;

    @ApiModelProperty(value = "悬赏价格")
    private BigDecimal releasePrice;

    @ApiModelProperty(value = "任务数量")
    private Integer jobNum;

    @ApiModelProperty(value = "剩余数量")
    private Integer surplusNum;

    @ApiModelProperty(value = "任务标签")
    private String typeName;

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value = "项目名称")
    private String jobSource;

    @ApiModelProperty(value = "报名时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date enrollTime;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date commitTime;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date auditTime;

    @ApiModelProperty(value = "状态（1.已报名；2.已过期;3.已提交；4.审核通过；5.审核拒绝；）")
    private Integer status;

    @ApiModelProperty(value = "不通过原因")
    private String refuseReason;

    @ApiModelProperty(value = "提交信息")
    private String commitInfo;

    @ApiModelProperty(value = "是否推荐（1不推荐，2推荐）")
    private Integer isRecommend;

    @ApiModelProperty(value = "是否是会员（1.不是会员；2.周会员；3.月会员；4.季会员；5.年会员）")
    private Integer isMember;
}
