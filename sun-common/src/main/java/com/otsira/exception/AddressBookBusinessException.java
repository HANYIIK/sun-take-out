package com.otsira.exception;

import lombok.NoArgsConstructor;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 地址簿为空不能下单自定义异常类
 * @create: 2024/11/23 11:58
 */
@NoArgsConstructor
public class AddressBookBusinessException extends BaseException {
        public AddressBookBusinessException(String msg) {
            super(msg);
        }
}
