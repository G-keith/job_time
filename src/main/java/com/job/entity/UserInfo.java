package com.job.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    private String openid;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "1.男，2女，0未知")
    private Integer sex;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "用户头像")
    private String headimgurl;

    @ApiModelProperty(value = "是否是管理员（0不是，1是）")
    private Integer isAdmin;

    @ApiModelProperty(value = "用户推广码")
    private String UID;

    @ApiModelProperty(value = "状态（0正常，1黑名单）")
    private Integer status;

    @ApiModelProperty(value = "上级用户推广码")
    private String upUID;

    @ApiModelProperty(value = "周会员有效期")
    private Date weekMemberTime;
    @ApiModelProperty(value = "月会员有效期")
    private Date monthMemberTime;
    @ApiModelProperty(value = "季会员有效期")
    private Date seasonMemberTime;
    @ApiModelProperty(value = "年会员有效期")
    private Date yearMemberTime;
    @ApiModelProperty(value = "是否是会员（1.不是会员；2.周会员；3.月会员；4.季会员；5.年会员）")
    private Integer isMember;

    @ApiModelProperty(value = "黑名单原因")
    private String reason;

    @ApiModelProperty(value = "是否第一次登录（1，是，2不是）")
    private Integer isFirst;
}