package com.otsira.service.impl;

import com.otsira.mapper.DishMapper;
import com.otsira.mapper.OrderMapper;
import com.otsira.mapper.SetmealMapper;
import com.otsira.service.WorkspaceService;
import com.otsira.vo.OrderOverViewVO;
import com.otsira.vo.OverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
