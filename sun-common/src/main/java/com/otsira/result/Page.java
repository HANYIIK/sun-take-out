package com.otsira.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 分页数据
 * @create: 2024/10/27 23:21
 */
@SuppressWarnings("rawtypes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "后端响应给前端的统一分页结果类")
public class Page implements Serializable {
    @ApiModelProperty("总记录数")
    private long total;

    @ApiModelProperty("当前页数据的集合")
    private List records;
}
