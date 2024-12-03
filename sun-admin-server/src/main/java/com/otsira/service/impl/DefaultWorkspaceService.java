package com.otsira.service.impl;

import com.otsira.client.UserClient;
import com.otsira.entity.Order;
import com.otsira.mapper.DishMapper;
import com.otsira.mapper.OrderMapper;
import com.otsira.mapper.SetmealMapper;
import com.otsira.service.WorkspaceService;
import com.otsira.vo.BusinessDataVO;
import com.otsira.vo.OrderOverViewVO;
import com.otsira.vo.OverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 工作台相关功能的 service 层接口实现类
 * @create: 2024/11/30 18:30
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class DefaultWorkspaceService implements WorkspaceService {
    private DishMapper dishMapper;
    private SetmealMapper setmealMapper;
    private OrderMapper orderMapper;
    private UserClient userClient;

    @Autowired
    public void setDishMapper(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setSetmealMapper(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    @Autowired
    public void setUserClient(@Qualifier("userClient") UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public OverViewVO overviewDishes() {
        Integer sold = dishMapper.countSold();
        Integer discontinued = dishMapper.countDiscontinued();
        return OverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public OverViewVO overviewSetmeals() {
        Integer sold = setmealMapper.countSold();
        Integer discontinued = setmealMapper.countDiscontinued();
        return OverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public OrderOverViewVO overviewOrders() {
        Integer allOrders = orderMapper.countAllOrders();
        Integer cancelledOrders = orderMapper.countCancelledOrders();
        Integer completedOrders = orderMapper.countCompletedOrders();
        Integer deliveredOrders = orderMapper.countDeliveredOrders();
        Integer waitingOrders = orderMapper.countWaitingOrders();
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }

    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        // 1.新增用户数
        // 使用 openFeign 请求用户端微服务查询新用户数
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String beginStr = begin.format(formatter);
        String endStr = end.format(formatter);
        // userClient 的 findNewAndTotalUserNum() 方法需要 String 类型的参数
        Integer newUserNum = userClient.findNewAndTotalUserNum(beginStr, endStr).get(0);

        // 2.有效订单数
        Integer completeOrderCount = orderMapper.countValidOrderByDate(begin, end, Order.COMPLETED);

        // 3.总订单数
        Integer totalOrderCount = orderMapper.countAllOrdersByDate(begin, end);

        // 4.订单完成率
        double orderCompletionRate;
        if (totalOrderCount != null && totalOrderCount != 0) {
            orderCompletionRate = BigDecimal.valueOf((double) completeOrderCount / (double) totalOrderCount)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        } else {
            orderCompletionRate = 0.0;
        }

        // 5.营业额
        Double turnover = orderMapper.countTurnoverByDate(begin, end);

        // 6.平均客单价
        double unitPrice;
        if (completeOrderCount != null && completeOrderCount != 0) {
            unitPrice = BigDecimal.valueOf(turnover / completeOrderCount)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        } else {
            unitPrice = 0.0;
        }

        return BusinessDataVO.builder()
                // 新增用户数
                .newUsers(newUserNum)
                // 订单完成率
                .orderCompletionRate(orderCompletionRate)
                // 营业额
                .turnover(turnover)
                // 平均客单价
                .unitPrice(unitPrice)
                // 有效订单数
                .validOrderCount(completeOrderCount)
                .build();
    }
}
