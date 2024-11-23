package com.otsira.service;

import com.otsira.dto.OrderSubmitDTO;
import com.otsira.vo.OrderSubmitVO;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户下单相关功能的 service 层接口
 * @create: 2024/11/22 18:53
 */
public interface OrderService {
    OrderSubmitVO submitOrder(OrderSubmitDTO orderSubmitDTO);
}
