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

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于封装数据库中订单详情表的实体类
 * @create: 2024/11/22 18:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("用于封装数据库中订单详情表的实体类")
@Table(name = "order_detail")
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("菜品id")
    private Long dishId;

    @ApiModelProperty("套餐id")
    private Long setmealId;

    @ApiModelProperty("口味")
    private String dishFlavor;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("菜品/套餐单价")
    private BigDecimal amount;

    @ApiModelProperty("图片")
    private String image;
}
