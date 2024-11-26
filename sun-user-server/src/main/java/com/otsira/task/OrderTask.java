package com.otsira.task;

import com.otsira.entity.Order;
import com.otsira.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 订单定时任务(超时未付款订单, 长期处于派送中的订单)
 * @create: 2024/11/26 17:39
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
@Slf4j
public class OrderTask {
    private OrderMapper orderMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 处理超过 15 分钟未付款订单, 系统自动取消订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        log.info("处理超时未付款订单");
        // 15分钟未付款, 系统判定为超时订单, 自动取消订单
        LocalDateTime timeoutTime = LocalDateTime.now().plusMinutes(-15);
        List<Order> timeoutOrderList = orderMapper.queryByStatusAndOrderTime(Order.PENDING_PAYMENT, timeoutTime);
        if (timeoutOrderList != null && !timeoutOrderList.isEmpty()) {
            timeoutOrderList.forEach(order -> {
                order.setStatus(Order.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("超时未付款, 系统自动取消订单");
                orderMapper.updateByPrimaryKeySelective(order);
            });
        }
    }

    /**
     * 在每天的凌晨 1 点, 处理长期处于派送中的订单, 系统自动完成订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("处理长期处于派送中的订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Order> orderList = orderMapper.queryByStatusAndOrderTime(Order.DELIVERY_IN_PROGRESS, time);
        if (orderList != null && !orderList.isEmpty()) {
            orderList.forEach(order -> {
                order.setStatus(Order.COMPLETED);
                orderMapper.updateByPrimaryKeySelective(order);
            });
        }
    }
}
