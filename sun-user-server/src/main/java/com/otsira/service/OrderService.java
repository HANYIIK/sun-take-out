package com.otsira.service;

import com.otsira.dto.OrderSubmitDTO;
import com.otsira.dto.OrdersPaymentDTO;
import com.otsira.result.Page;
import com.otsira.vo.OrderPaymentVO;
import com.otsira.vo.OrderSubmitVO;
import com.otsira.vo.OrderWithDetailsVO;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户下单相关功能的 service 层接口
 * @create: 2024/11/22 18:53
 */
public interface OrderService {
    OrderSubmitVO submitOrder(OrderSubmitDTO orderSubmitDTO);
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
    int paySuccess(String outTradeNo);
    Page historyOrders(Integer page, Integer pageSize, Integer status);
    OrderWithDetailsVO orderDetail(Long id);
    int cancelOrder(Long id);
    int repeatOrder(Long id);
    boolean reminder(Long id);
}
