package com.job.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Data
public class UserOpinion {

    @ApiModelProperty(value = "主键id")
    private Integer opinionId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "1.未处理，2已处理")
    private Integer status;

    @ApiModelProperty(value = "意见")
    private String opinion;

    @ApiModelProperty(value = "回复")
    private String reply;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date commitTime;

    @ApiModelProperty(value = "反馈时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date replyTime;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String headimgurl;

    @ApiModelProperty(value = "手机号")
    private String phone;

}