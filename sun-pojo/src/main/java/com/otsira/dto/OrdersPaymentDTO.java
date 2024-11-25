package com.otsira.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 前端发送给后端的订单支付 DTO
 * @create: 2024/11/24 17:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("前端发送给后端的订单支付 DTO")
public class OrdersPaymentDTO implements Serializable {
    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("付款方式: 1-微信 2-支付宝")
    private Integer payMethod;
}
