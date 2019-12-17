package com.job.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 点检项设置实体类
 *
 * @author ziv
 * @since 2019-06-17
 */
@Data
@ApiModel(value = "点检项设置实体")
public class EquipCheckItem {
    /**
     * 点检项主键id
     */
    @ApiParam(hidden = true)
    @ApiModelProperty(value = "点检项主键id")
    private Long itemId;
    
    /**
     * 点检项编号
     */
    @ApiParam(value = "点检项编号", required = true)
    @ApiModelProperty(value = "点检项编号")
    private String itemCode;
    
    /**
     * 点检项名称
     */
    @ApiParam(value = "点检项名称", required = true)
    @ApiModelProperty(value = "点检项名称")
    private String itemName;
    
    /**
     * 状态 0:正常  1：停用
     */
    @ApiParam(value = "状态 0:正常  1：停用", required = true)
    @ApiModelProperty(value = "状态")
    private Integer itemStatus;
    
    /**
     * 点检项描述
     */
    @ApiParam(value = "点检项描述")
    @ApiModelProperty(value = "点检项描述")
    private String itemDesc;
    
}