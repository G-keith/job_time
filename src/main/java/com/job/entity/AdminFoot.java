package com.job.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/6
 */
@Data
public class AdminFoot {

    @ApiModelProperty(value = "主键d")
    private Integer footId;

    @ApiModelProperty(value = "管理员账户")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;
}
