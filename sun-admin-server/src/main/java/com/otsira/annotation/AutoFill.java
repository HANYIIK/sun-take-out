package com.otsira.annotation;

import com.otsira.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: insert 和 update 时, 自动填充 createUser/createTime, updateUser/updateTime
 * @create: 2024/10/30 19:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    /**
     * 枚举操作类型: INSERT / UPDATE
     */
    OperationType value();
}
