package com.job.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CashOutOrderVo {

    @ApiModelProperty(value = "主键id")
    private Integer cashOutId;

    @ApiModelProperty(value = "提现金额(元)")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "打款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date payTime;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户头像")
    private String headimgurl;

    @ApiModelProperty(value = "昵称")
    private String nickname;

}
