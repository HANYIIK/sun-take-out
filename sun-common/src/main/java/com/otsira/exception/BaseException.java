package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 自定义业务异常类的基类
 * @create: 2024/10/20 14:50
 */
@NoArgsConstructor
public class BaseException extends RuntimeException {
    public BaseException(String msg) {
        super(msg);
    }
}
