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

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description:
 * @create: 2024/10/31 18:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dish_flavor")
@ApiModel(description = "用于封装数据库中菜品口味的实体类")
public class DishFlavor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("菜品id")
    private Long dishId;

    @ApiModelProperty("口味名称")
    private String name;

    @ApiModelProperty("口味数据list: [无糖, 少糖, 半糖, 多糖, 全糖]")
    private String value;
}
