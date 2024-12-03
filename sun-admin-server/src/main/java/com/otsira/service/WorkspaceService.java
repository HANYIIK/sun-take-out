package com.otsira.service;

import com.otsira.vo.BusinessDataVO;
import com.otsira.vo.OrderOverViewVO;
import com.otsira.vo.OverViewVO;

import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 工作台相关功能的 service 层接口
 * @create: 2024/11/30 18:30
 */
public interface WorkspaceService {
    OverViewVO overviewDishes();
    OverViewVO overviewSetmeals();
    OrderOverViewVO overviewOrders();
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);
}
