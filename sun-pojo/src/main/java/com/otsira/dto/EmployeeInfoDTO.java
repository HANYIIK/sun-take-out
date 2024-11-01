package com.otsira.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于封装前端发过来的新增员工表单的 DTO
 * @create: 2024/10/27 18:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用于封装前端发过来的新增/修改员工表单的 DTO")
public class EmployeeInfoDTO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("状态: 0-禁用, 1-启用")
    private Integer status;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建该员工信息的员工id")
    private Long createUser;

    @ApiModelProperty("修改该员工信息的员工id")
    private Long updateUser;
}
