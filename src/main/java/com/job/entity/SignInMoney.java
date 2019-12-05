package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/3
 */
@Data
public class SignInMoney {

    @ApiModelProperty(value = "签到金额")
    private BigDecimal money;
    @ApiModelProperty(value = "一级推广赏金比例")
    private BigDecimal oneInvite;
    @ApiModelProperty(value = "二级推广赏金比例")
    private BigDecimal twoInvite;
}
