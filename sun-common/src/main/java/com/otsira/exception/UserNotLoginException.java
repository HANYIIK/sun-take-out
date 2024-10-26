package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户未登录异常
 * @create: 2024/10/25 19:33
 */
@NoArgsConstructor
public class UserNotLoginException extends BaseException {
    public UserNotLoginException(String msg) {
        super(msg);
    }
}
