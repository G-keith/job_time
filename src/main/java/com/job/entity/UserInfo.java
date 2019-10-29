package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户信息实体
 * @author keith
 * @version 1.0
 * @date 2019-10-28
 */
@Data
public class UserInfo {

    @ApiModelProperty(value = "主键id")
    private Integer userId;

    @ApiModelProperty(value = "账号")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "是否是管理员（0不是，1是）")
    private Integer isAdmin;

    @ApiModelProperty(value = "头像")
    private String head;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
}