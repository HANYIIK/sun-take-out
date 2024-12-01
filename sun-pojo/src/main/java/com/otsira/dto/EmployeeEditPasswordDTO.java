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
 * @description: 前端给后端传过来的员工修改密码的DTO
 * @create: 2024/12/01 18:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("前端给后端传过来的员工修改密码的DTO")
public class EmployeeEditPasswordDTO implements Serializable {
    @ApiModelProperty("员工id")
    private Integer empId;

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("旧密码")
    private String oldPassword;
}
