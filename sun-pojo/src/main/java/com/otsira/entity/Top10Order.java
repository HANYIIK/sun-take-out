package com.otsira.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 销量前十的菜品/套餐名称和其对应的销量
 * @create: 2024/12/03 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("销量排名前10的菜品或套餐")
public class Top10Order implements Serializable {
    @ApiModelProperty("菜品或套餐名称")
    private String name;

    @ApiModelProperty("销量")
    private Integer total;
}
