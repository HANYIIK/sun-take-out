package com.otsira.exception;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品名重复自定义异常类
 * @create: 2024/11/03 20:20
 */
public class DishNameConflictException extends BaseException {
    public DishNameConflictException(String msg) {
        super(msg);
    }
}
