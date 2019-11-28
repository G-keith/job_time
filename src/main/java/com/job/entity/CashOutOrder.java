package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/17
 */
@Data
public class CashOutOrder {

    @ApiModelProperty(value = "主键id")
    private Integer cashOutId;

    @ApiModelProperty(value = "企业付款备注")
    private String remarks;

    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    @ApiModelProperty(value = "提现金额(元)")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "用户openid")
    private String openid;

    @ApiModelProperty(value = "支付宝姓名")
    private String zfbName;

    @ApiModelProperty(value = "支付宝账户")
    private String zfbAccount;

    @ApiModelProperty(value = "打款时间")
    private Date payTime;
}
