package com.otsira.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 前端返回给后端的订单提交DTO
 * @create: 2024/11/22 18:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("前端返回给后端的订单提交DTO")
public class OrderSubmitDTO implements Serializable {
    @ApiModelProperty("地址簿id")
    private Long addressBookId;

    @ApiModelProperty("支付方式: 1-微信 2-支付宝")
    private int payMethod;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("预计送达时间")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;

    @ApiModelProperty("配送状态: 1-立即送出 0-选择具体时间")
    private Integer deliveryStatus;

    @ApiModelProperty("餐具数量")
    private Integer tablewareNumber;

    @ApiModelProperty("餐具数量状态: 1-按餐量提供 0-选择具体数量")
    private Integer tablewareStatus;

    @ApiModelProperty("打包费")
    private Integer packAmount;

    @ApiModelProperty("总金额")
    private BigDecimal amount;
}
