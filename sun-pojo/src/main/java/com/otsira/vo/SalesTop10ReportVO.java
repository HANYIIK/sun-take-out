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
 * @description: 后端返回给前端的销量排名前十的数据VO
 * @create: 2024/12/03 12:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的销量排名前十的数据VO")
public class SalesTop10ReportVO implements Serializable {
    @ApiModelProperty("商品名称列表，以逗号分隔")
    private String nameList;

    @ApiModelProperty("销量列表，以逗号分隔")
    private String numberList;
}
