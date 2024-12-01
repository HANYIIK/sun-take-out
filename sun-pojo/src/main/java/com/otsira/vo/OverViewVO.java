package com.otsira.vo;

import io.swagger.annotations.Api;
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
 * @description: 后端返回给前端的菜品/套餐起售/停售数量VO
 * @create: 2024/11/30 19:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的菜品/套餐起售/停售数量VO")
public class OverViewVO implements Serializable {
    @ApiModelProperty("起售数量")
    private Integer sold;

    @ApiModelProperty("停售数量")
    private Integer discontinued;

}
