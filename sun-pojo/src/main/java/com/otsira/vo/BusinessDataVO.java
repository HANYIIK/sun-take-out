package com.otsira.vo;

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
 * @description: 后端返回给前端的今日运营数据VO
 * @create: 2024/12/03 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的今日运营数据VO")
public class BusinessDataVO implements Serializable {
    @ApiModelProperty("新增用户数")
    private Integer newUsers;

    @ApiModelProperty("订单完成率")
    private Double orderCompletionRate;

    @ApiModelProperty("营业额")
    private Double turnover;

    @ApiModelProperty("平均客单价")
    private Double unitPrice;

    @ApiModelProperty("有效订单数")
    private Integer validOrderCount;
}
