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
 * @description: 后端传递给前端的各个状态的订单数量统计VO
 * @create: 2024/11/30 16:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端传递给前端的各个状态的订单数量统计VO")
public class OrderStatisticsVO implements Serializable {
    @ApiModelProperty("待接单-订单数量")
    private Integer toBeConfirmed;

    @ApiModelProperty("待派送-订单数量")
    private Integer confirmed;

    @ApiModelProperty("派送中-订单数量")
    private Integer deliveryInProgress;

}
