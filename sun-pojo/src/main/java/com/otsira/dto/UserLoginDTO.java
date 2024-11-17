package com.otsira.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 前端返回过来的用户登录的 DTO
 * @create: 2024/11/16 12:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户登录的前端 POST 传输类")
public class UserLoginDTO {
    @ApiModelProperty("微信授权码")
    private String code;
}
