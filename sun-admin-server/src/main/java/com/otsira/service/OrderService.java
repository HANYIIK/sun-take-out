package com.otsira.service;

import com.otsira.entity.Order;
import com.otsira.result.Page;
import com.otsira.vo.OrderStatisticsVO;
import com.otsira.vo.OrderWithDetailsVO;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 订单相关功能的 service 层接口
 * @create: 2024/11/29 17:27
 */
public interface OrderService {
    Page queryAllOrders(Integer page, Integer pageSize, String number, String phone, String beginTime, String endTime, String status);
    OrderWithDetailsVO queryOrderDetailsById(Long id);
    int confirmOrder(Long id);
    int cancelOrder(Order order);
    int rejectOrder(Order order);
    int deliveryOrder(Long id);
    int completeOrder(Long id);
    OrderStatisticsVO countOrderStatistics();
}
