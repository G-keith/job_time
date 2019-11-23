package com.job.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/5
 */
@Data
public class InviteVo {

    @ApiModelProperty(value = "主键id")
    private Integer userId;

    @ApiModelProperty(value = "用户头像")
    private String headimgurl;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "总人数")
    private Integer totalNum;

    @ApiModelProperty(value = "总获得奖励")
    private BigDecimal totalMoney;

    @ApiModelProperty(value = "手机号")
    private String phone;
}
