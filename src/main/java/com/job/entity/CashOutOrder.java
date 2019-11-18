package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/17
 */
@Data
public class CashOutOrder {

    @ApiModelProperty(value = "企业付款备注")
    private String desc;

    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    @ApiModelProperty(value = "商品总价（分）")
    private Integer totalFee;

    @ApiModelProperty(value = "用户openid")
    private String openid;
}
