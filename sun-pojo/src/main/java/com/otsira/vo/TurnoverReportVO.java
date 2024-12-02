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
 * @description: 后端返回给前端的营业额统计信息VO
 * @create: 2024/12/01 21:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的营业额统计信息VO")
public class TurnoverReportVO implements Serializable {
    @ApiModelProperty("日期列表, 以逗号分隔")
    private String dateList;

    @ApiModelProperty("营业额列表, 以逗号分隔")
    private String turnoverList;
}
