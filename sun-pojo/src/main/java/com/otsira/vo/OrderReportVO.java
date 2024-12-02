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
 * @description: 后端返回给前端的订单统计信息VO
 * @create: 2024/12/01 21:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的订单统计信息VO")
public class OrderReportVO implements Serializable {
    @ApiModelProperty("日期列表, 以逗号分隔")
    private String dateList;

    @ApiModelProperty("订单完成率")
    private Double orderCompletionRate;

    @ApiModelProperty("订单总数")
    private Integer totalOrderCount;

    @ApiModelProperty("订单数列表, 以逗号分隔")
    private String orderCountList;

    @ApiModelProperty("有效订单数")
    private Integer validOrderCount;

    @ApiModelProperty("有效订单数列表, 以逗号分隔")
    private String validOrderCountList;
}
