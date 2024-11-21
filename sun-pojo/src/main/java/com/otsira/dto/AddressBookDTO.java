package com.otsira.dto;

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
 * @description: 前端传给后端的新增/修改地址簿的 DTO
 * @create: 2024/11/21 20:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用于封装前端传给后端的新增/修改地址簿的 DTO")
public class AddressBookDTO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("性别 0 女 1 男")
    private String sex;

    @ApiModelProperty("省级区划编号")
    private String provinceCode;

    @ApiModelProperty("省级名称")
    private String provinceName;

    @ApiModelProperty("市级区划编号")
    private String cityCode;

    @ApiModelProperty("市级名称")
    private String cityName;

    @ApiModelProperty("区级区划编号")
    private String districtCode;

    @ApiModelProperty("区级名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("类型")
    private String type;

}
