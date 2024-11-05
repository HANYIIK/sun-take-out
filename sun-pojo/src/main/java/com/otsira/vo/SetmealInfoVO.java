package com.otsira.vo;

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
 * @description: 后端返回给前端的套餐全列表VO(需要categoryName属性)
 * @create: 2024/11/04 20:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "后端返回给前端的套餐全列表VO(需要categoryName属性)")
public class SetmealInfoVO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("套餐分类id")
    private Long categoryId;

    @ApiModelProperty("套餐分类名称")
    private String categoryName;

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

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建该项套餐的员工id")
    private Long createUser;

    @ApiModelProperty("更新该项套餐的员工id")
    private Long updateUser;
}
