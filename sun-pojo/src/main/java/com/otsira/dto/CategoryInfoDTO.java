package com.otsira.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 前端传输过来的菜品分类封装类（包括 name, sort, type）
 * @create: 2024/10/30 17:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用于封装前端发过来的新增/修改菜品分类表单的 DTO")
public class CategoryInfoDTO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("菜品分类名")
    private String name;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态: 0-禁用, 1-启用")
    private Integer status;

    @ApiModelProperty("分类类型: 1-菜品分类, 2-套餐分类")
    private Integer type;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建该项分类的员工id")
    private Long createUser;

    @ApiModelProperty("修改该项分类的员工id")
    private Long updateUser;
}
