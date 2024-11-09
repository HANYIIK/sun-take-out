package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 套餐中包含未起售的菜品，套餐无法起售自定义异常类
 * @create: 2024/11/07 17:29
 */
@NoArgsConstructor
public class EnableStatusNotAllowedException extends BaseException {
    public EnableStatusNotAllowedException(String msg) {
        super(msg);
    }
}
