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
 * @description: 菜品的数据库封装类
 * @create: 2024/10/31 18:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dish")
@ApiModel(description = "用于封装数据库中菜品的实体类")
public class Dish implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty(value = "菜品名称")
    private String name;

    @ApiModelProperty(value = "菜品分类id")
    private Long categoryId;

    @ApiModelProperty(value = "菜品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "菜品图片")
    private String image;

    @ApiModelProperty(value = "菜品描述信息")
    private String description;

    @ApiModelProperty(value = "菜品状态: 0-停售, 1-起售")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建该项菜品的员工id")
    private Long createUser;

    @ApiModelProperty(value = "修改该项菜品的员工id")
    private Long updateUser;
}
