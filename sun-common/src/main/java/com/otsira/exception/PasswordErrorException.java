package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 自定义密码错误异常
 * @create: 2024/10/20 14:52
 */
@NoArgsConstructor
public class PasswordErrorException extends BaseException {
    public PasswordErrorException(String msg) {
        super(msg);
    }
}