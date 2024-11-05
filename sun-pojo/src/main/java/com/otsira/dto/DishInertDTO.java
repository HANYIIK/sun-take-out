package com.otsira.dto;

import com.otsira.entity.DishFlavor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* @program: sun-take-out
* @author: HANYIIK
* @description: 用于封装前端发过来的新增菜品表单的 DTO(包含口味 dishFlavors)
* @create: 2024/11/03 18:56
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用于封装前端发过来的新增菜品表单的 DTO(包含口味 dishFlavors)")
public class DishInertDTO {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("状态码")
    private String code;

    @ApiModelProperty("菜品名称")
    private String name;

    @ApiModelProperty("菜品分类id")
    private Long categoryId;

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
