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
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品分类的数据库封装实体类
 * @create: 2024/10/30 16:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category")
@ApiModel(description = "用于封装数据库中菜品分类的实体类")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("类型: 1-菜品分类, 2-套餐分类")
    private Integer type;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("分类状态 0-禁用, 1-启用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建该项分类的员工id")
    private Long createUser;

    @ApiModelProperty("修改该项分类的员工id")
    private Long updateUser;
}
