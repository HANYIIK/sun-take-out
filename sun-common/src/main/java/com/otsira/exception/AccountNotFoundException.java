package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 自定义用户在数据库中不存在异常
 * @create: 2024/10/20 14:53
 */
@NoArgsConstructor
public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
