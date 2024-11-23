package com.otsira.vo;

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
 * @description: 后端返回给前端的订单提交VO
 * @create: 2024/11/22 18:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的订单提交VO")
public class OrderSubmitVO implements Serializable {
    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;
}
