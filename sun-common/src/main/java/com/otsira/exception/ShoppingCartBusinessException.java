package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 购物车为空不能下单自定义异常类
 * @create: 2024/11/23 12:00
 */
@NoArgsConstructor
public class ShoppingCartBusinessException extends BaseException {
        public ShoppingCartBusinessException(String msg) {
            super(msg);
        }
}
