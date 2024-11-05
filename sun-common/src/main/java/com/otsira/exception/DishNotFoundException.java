package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品未找到自定义异常类
 * @create: 2024/11/02 17:50
 */
@NoArgsConstructor
public class DishNotFoundException extends BaseException {
    public DishNotFoundException(String msg) {
        super(msg);
    }
}
