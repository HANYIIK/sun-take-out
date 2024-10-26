package com.otsira.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于封装数据库中的用户信息类
 * @create: 2024/10/26 17:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用于封装数据库中用户全部信息的实体类")
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("微信用户唯一标识")
    private String openid;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("性别")
    // 0-女
    // 1-男
    private String sex;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("注册时间")
    private LocalDateTime createTime;
}