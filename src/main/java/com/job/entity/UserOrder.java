package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 * @author keith
 * @version 1.0
 * @date 2019-11-18
 */
@Data
public class UserOrder {

    @ApiModelProperty(value = "主键id")
    private Integer orderId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @ApiModelProperty(value = "预支付id")
    private String prepayid;

    @ApiModelProperty(value = "订单号")
    private String orderNum;

    @ApiModelProperty(value = "订单描述")
    private String orderDesc;

    @ApiModelProperty(value = "订单状态（1.未付款；2.已付款；3.交易失败）")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单类型（1.会员充值，2.账户充值）")
    private Integer orderType;

    @ApiModelProperty(value = "订单提交时间")
    private Date commitTime;

    @ApiModelProperty(value = "订单完成时间")
    private Date finishTime;
}