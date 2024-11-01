package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品分类名称冲突自定义异常
 * @create: 2024/10/30 17:42
 */
@NoArgsConstructor
public class CategoryNameConflictException extends BaseException {
    public CategoryNameConflictException(String msg) {
        super(msg);
    }
}

