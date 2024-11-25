package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 订单支付自定义异常类
 * @create: 2024/11/24 17:28
 */
@NoArgsConstructor
public class OrderBusinessException extends BaseException {
    public OrderBusinessException(String msg) {
        super(msg);
    }
}
