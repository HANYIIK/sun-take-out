package com.otsira.vo;

import com.otsira.entity.OrderDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 后端返回给前端的包含订单明细的订单VO
 * @create: 2024/11/26 13:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的包含订单明细的订单VO")
public class OrderWithDetailsVO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("订单号")
    private String number;

    @ApiModelProperty("订单状态: 1-待付款 2-待接单 3-已接单 4-派送中 5-已完成 6-已取消 7-退款")
    private Integer status;

    @ApiModelProperty("下单用户id")
    private Long userId;

    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;

    @ApiModelProperty("结账时间")
    private LocalDateTime checkoutTime;

    @ApiModelProperty("支付状态: 0-未支付 1-已支付 2-退款")
    private Integer payStatus;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("订单取消原因")
    private String cancelReason;

    @ApiModelProperty("订单拒绝原因")
    private String rejectionReason;

    @ApiModelProperty("订单取消时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty("送达时间")
    private LocalDateTime deliveryTime;


    @ApiModelProperty("地址簿id")
    private Long addressBookId;

    @ApiModelProperty("支付方式: 1-微信 2-支付宝")
    private Integer payMethod;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("预计送达时间")
    private LocalDateTime estimatedDeliveryTime;

    @ApiModelProperty("配送状态: 1-立即送出 0-选择具体时间")
    private Integer deliveryStatus;

    @ApiModelProperty("餐具数量")
    private int tablewareNumber;

    @ApiModelProperty("餐具数量状态: 1-按餐量提供 0-选择具体数量")
    private Integer tablewareStatus;

    @ApiModelProperty("打包费")
    private int packAmount;

    @ApiModelProperty("总金额")
    private BigDecimal amount;

    @ApiModelProperty("订单明细")
    private List<OrderDetail> orderDetailList;
}
