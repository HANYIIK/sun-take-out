package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户登录失败的自定义异常类
 * @create: 2024/11/16 13:11
 */
@NoArgsConstructor
public class LoginFailedException extends BaseException {
    public LoginFailedException(String msg) {
        super(msg);
    }
}
