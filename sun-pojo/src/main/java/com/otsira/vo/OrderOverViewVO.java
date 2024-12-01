package com.otsira.vo;

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
 * @description: 后端返回给前端的订单总览数据VO
 * @create: 2024/11/30 19:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("后端返回给前端的订单总览数据VO")
public class OrderOverViewVO implements Serializable {
    @ApiModelProperty("全部订单")
    private Integer allOrders;

    @ApiModelProperty("已取消数量")
    private Integer cancelledOrders;

    @ApiModelProperty("已完成数量")
    private Integer completedOrders;

    @ApiModelProperty("待派送数量")
    private Integer deliveredOrders;

    @ApiModelProperty("待接单数量")
    private Integer waitingOrders;
}
