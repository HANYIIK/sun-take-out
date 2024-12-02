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
 * @description: 后端返回给前端的用户统计信息VO
 * @create: 2024/12/01 21:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("后端返回给前端的用户统计信息VO")
public class UserReportVO implements Serializable {
    @ApiModelProperty("日期列表, 以逗号分隔")
    private String dateList;

    @ApiModelProperty("新增用户数列表, 以逗号分隔")
    private String newUserList;

    @ApiModelProperty("总用户量列表, 以逗号分隔")
    private String totalUserList;
}
