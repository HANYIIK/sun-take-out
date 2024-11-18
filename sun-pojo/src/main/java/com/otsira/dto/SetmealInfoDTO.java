package com.otsira.dto;

import com.otsira.entity.SetmealDish;
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
 * @description: 用于封装前端传过来的详细套餐DTO(包括套餐中的菜品信息)
 * @create: 2024/11/04 21:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用于封装前端传过来的详细套餐DTO(包括套餐中的菜品信息)")
public class SetmealInfoDTO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("套餐名称")
    private String name;

    @ApiModelProperty("套餐价格")
    private BigDecimal price;

    @ApiModelProperty("套餐状态: 0-停售, 1-起售")
    private Integer status;

    @ApiModelProperty("套餐描述信息")
    private String description;

    @ApiModelProperty("套餐图片")
    private String image;

    @ApiModelProperty("套餐中包含的菜品列表")
    private List<SetmealDish> setmealDishes;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建该项套餐的员工id")
    private Long createUser;

    @ApiModelProperty("更新该项套餐的员工id")
    private Long updateUser;
}
