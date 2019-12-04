package com.job.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Data
public class ServiceFee {

    @ApiModelProperty(value = "主键id")
    private Integer serviceId;
    @ApiModelProperty(value = "服务费类型(1：代表发布任务服务费；2.提现任务奖励时服务费；3.充值金额提现服务费)")
    private Integer serviceType;
    @ApiModelProperty(value = "普通用户比例")
    private BigDecimal commonRate;
    @ApiModelProperty(value = "周会员用户比例")
    private BigDecimal weekRate;
    @ApiModelProperty(value = "月会员用户比例")
    private BigDecimal monthRate;
    @ApiModelProperty(value = "季会员用户比例")
    private BigDecimal seasonRate;
    @ApiModelProperty(value = "年会员用户比例")
    private BigDecimal yearRate;
    @ApiModelProperty(value = "备注")
    private String remarks;
}
