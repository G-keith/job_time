package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/15
 */
@Data
public class UserOrder {

    @ApiModelProperty(value = "商品描述")
    private String introduce;

    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    @ApiModelProperty(value = "商品总价（分）")
    private Integer totalFee;
}
