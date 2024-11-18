package com.otsira.vo;

import com.otsira.entity.DishFlavor;
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
 * @description: 后端返回给前端的 Dish 数据(需要categoryName属性)
 * @create: 2024/11/03 19:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "后端返回给前端的 Dish 数据(需要categoryName属性)")
public class DishWithFlavorsAndCategoryNameVO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("菜品名称")
    private String name;

    @ApiModelProperty("菜品分类id")
    private Long categoryId;

    @ApiModelProperty(value = "菜品分类名称")
    private String categoryName;

    @ApiModelProperty("菜品价格")
    private BigDecimal price;

    @ApiModelProperty("菜品图片")
    private String image;

    @ApiModelProperty("菜品描述信息")
    private String description;

    @ApiModelProperty("菜品状态: 0-停售, 1-起售")
    private Integer status;

    @ApiModelProperty("口味")
    private List<DishFlavor> flavors;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建该项菜品的员工id")
    private Long createUser;

    @ApiModelProperty("修改该项菜品的员工id")
    private Long updateUser;
}
