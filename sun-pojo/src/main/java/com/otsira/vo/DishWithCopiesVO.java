package com.otsira.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 套餐中的菜品查询, 带有套餐中的菜品份数
 * @create: 2024/11/16 19:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "后端返回给前端的套餐中所有的菜品封装类VO, 带有套餐中的菜品份数")
public class DishWithCopiesVO {
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

    @ApiModelProperty(value = "菜品份数")
    private Integer copies;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建该项菜品的员工id")
    private Long createUser;

    @ApiModelProperty(value = "修改该项菜品的员工id")
    private Long updateUser;
}
