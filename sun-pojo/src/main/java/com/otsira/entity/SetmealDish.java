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
import java.math.BigDecimal;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于封装数据库中套餐表中的菜品实体类
 * @create: 2024/11/04 19:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "setmeal_dish")
@ApiModel(description = "用于封装数据库中套餐表中的菜品实体类")
public class SetmealDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("套餐id")
    private Long setmealId;

    @ApiModelProperty("菜品id")
    private Long dishId;

    @ApiModelProperty("菜品名称")
    private String name;

    @ApiModelProperty("菜品原价")
    private BigDecimal price;

    @ApiModelProperty("份数")
    private Integer copies;
}
