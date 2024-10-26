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
 * @description: 登录成功后，后端给前端发送的管理员信息（data）
 * @create: 2024/10/19 21:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工登录成功后给前端返回的数据格式")
public class EmployeeLoginVO implements Serializable {
        @ApiModelProperty("主键值")
        private Long id;

        @ApiModelProperty("用户名")
        private String userName;

        @ApiModelProperty("姓名")
        private String name;

        @ApiModelProperty("jwt令牌")
        private String token;
}
