package com.otsira.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 后端响应给前端的统一结果类
 * @create: 2024/10/19 18:46
 */
@Data
@ApiModel(description = "后端响应给前端的统一结果类")
public class Result<T> implements Serializable {
    @ApiModelProperty("响应结果")
    private Integer code;

    @ApiModelProperty("响应消息")
    private String msg;

    @ApiModelProperty("响应数据")
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(1);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(1);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
}
