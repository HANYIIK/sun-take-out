package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品分类不存在的自定义异常类
 * @create: 2024/10/30 18:25
 */
@NoArgsConstructor
public class CategoryNotFoundException extends BaseException {
    public CategoryNotFoundException(String msg) {
        super(msg);
    }
}
