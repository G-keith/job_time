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
public class UserChatRecordVo {

    @ApiModelProperty(value = "发送方id")
    private Integer sendId;

    @ApiModelProperty(value = "接收方id")
    private Integer receiveId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date chatTime;

    @ApiModelProperty(value = "发送方头像")
    private String sendImg;

    @ApiModelProperty(value = "接收方头像")
    private String receiveImg;
}
