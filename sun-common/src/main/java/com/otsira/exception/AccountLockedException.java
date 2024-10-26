package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 自定义账户被禁用异常
 * @create: 2024/10/20 16:49
 */
@NoArgsConstructor
public class AccountLockedException extends BaseException {
    public AccountLockedException(String msg) {
        super(msg);
    }
}
