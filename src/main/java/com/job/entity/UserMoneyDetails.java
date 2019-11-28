package com.job.entity;

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
public class UserMoneyDetails {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "账户类型(1.保证金；2.任务余额；3.充值余额；4.奖金余额)")
    private Integer type;

    @ApiModelProperty(value = "说明")
    private String introduce;

    @ApiModelProperty(value = "金钱")
    private BigDecimal money;

    @ApiModelProperty(value = "交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date tradeTime;
}
