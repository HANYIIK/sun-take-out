package com.otsira.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于封装数据库中订单的实体类
 * @create: 2024/11/22 18:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("用于封装数据库中订单的实体类")
@Table(name = "orders")
public class Order implements Serializable {
    /**
     * 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;

    /**
     * 支付状态 0未支付 1已支付 2退款
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
