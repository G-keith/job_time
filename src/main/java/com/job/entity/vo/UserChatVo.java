package com.job.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/9
 */
@Data
public class UserChatVo {

    @ApiModelProperty(value = "主键id")
    private Integer newsId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "聊天对象id")
    private Integer targetId;

    @ApiModelProperty(value = "未读数量")
    private Integer newsNum;

    @ApiModelProperty(value = "最后发送消息")
    private String newsContent;

    @ApiModelProperty(value = "最后发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date newsTime;

    @ApiModelProperty(value = "聊天对象头像")
    private String targetImg;

    @ApiModelProperty(value = "聊天对象UID")
    private String UID;
}
