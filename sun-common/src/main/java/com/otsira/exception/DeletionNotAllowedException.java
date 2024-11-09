package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 该菜品分类下有绑定的菜品，或该菜品下有绑定的套餐，不允许删除自定义异常类
 * @create: 2024/11/05 20:33
 */
@NoArgsConstructor
public class DeletionNotAllowedException extends BaseException {
    public DeletionNotAllowedException(String msg) {
        super(msg);
    }
}
