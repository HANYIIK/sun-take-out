package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 自定义用户名冲突类，保证 username 的唯一性
 * @create: 2024/10/27 20:28
 */
@NoArgsConstructor
public class UserNameConflictException extends BaseException {
    public UserNameConflictException(String msg) {
        super(msg);
    }
}
