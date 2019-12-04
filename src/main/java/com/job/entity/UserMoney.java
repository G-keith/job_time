package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/29
 */
@Data
public class UserMoney {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "保证金")
    private BigDecimal bond;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "充值余额")
    private BigDecimal repaidBalance;

    @ApiModelProperty(value = "奖金余额")
    private BigDecimal bonus;
    @ApiModelProperty(value = "发布任务次数")
    private Integer jobNum;
    @ApiModelProperty(value = "刷新任务次数")
    private Integer refreshNum;
}
