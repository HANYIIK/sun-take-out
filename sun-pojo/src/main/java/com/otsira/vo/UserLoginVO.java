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
 * @description: 用户端登录用的后端返回给前端的封装类（id，openId，token）
 * @create: 2024/11/15 20:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户登录成功后给前端返回的数据格式")
public class UserLoginVO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("微信用户唯一标识")
    private String openid;

    @ApiModelProperty("jwt令牌")
    private String token;
}
