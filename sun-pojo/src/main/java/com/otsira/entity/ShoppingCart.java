package com.otsira.entity;

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
 * @description: 购物车中的菜品或套餐的实体类
 * @create: 2024/11/18 19:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "购物车中的菜品或套餐的实体类")
public class ShoppingCart implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("菜品id")
    private Long dishId;

    @ApiModelProperty("套餐id")
    private Long setmealId;

    @ApiModelProperty("口味")
    private String dishFlavor;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
