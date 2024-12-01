package com.otsira.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.entity.Dish;
import com.otsira.entity.Order;
import com.otsira.entity.OrderDetail;
import com.otsira.entity.Setmeal;
import com.otsira.mapper.DishMapper;
import com.otsira.mapper.OrderDetailMapper;
import com.otsira.mapper.OrderMapper;
import com.otsira.mapper.SetmealMapper;
import com.otsira.result.Page;
import com.otsira.service.OrderService;
import com.otsira.vo.OrderStatisticsVO;
import com.otsira.vo.OrderWithDishesStrVO;
import com.otsira.vo.OrderWithDetailsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 订单相关接口的 service 层默认实现类
 * @create: 2024/11/29 17:27
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class DefaultOrderService implements OrderService {
    private OrderMapper orderMapper;
    private OrderDetailMapper orderDetailMapper;
    private DishMapper dishMapper;
    private SetmealMapper setmealMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderDetailMapper(OrderDetailMapper orderDetailMapper) {
        this.orderDetailMapper = orderDetailMapper;
    }

    @Autowired
    public void setDishMapper(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Autowired
    public void setSetmealMapper(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 条件查询订单
     *
     * @param page     页码
     * @param pageSize 每页显示数量
     * @param number   订单号
     * @param phone    用户手机号
     * @param beginTime 下单时间范围: 起始
     * @param endTime 下单时间范围: 结束
     * @return 订单分页数据
     */
    @Override
    public Page queryAllOrders(Integer page, Integer pageSize, String number, String phone, String beginTime, String endTime, String status) {
        if (number == null) {
            number = "";
        }
        if (phone == null) {
            phone = "";
        }
        if (beginTime == null) {
            beginTime = "1970-01-01";
        }
        if (endTime == null) {
            endTime = "3000-01-01";
        }
        if (status == null) {
            status = "";
        }

        Integer total = orderMapper.queryPageCount(number, phone, beginTime, endTime, status);
        Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer start = (page - 1) * pageSize;

        List<Order> orders = orderMapper.queryOrderByPage(start, pageSize, number, phone, beginTime, endTime, status);
        List<OrderWithDishesStrVO> orderWithDishesStrVOList = new ArrayList<>();
        for (Order order : orders) {
            // TODO: 2024/11/29 17:27 查询订单详情
            List<OrderDetail> orderDetailList = orderDetailMapper.queryByOrderId(order.getId());
            StringBuilder orderDishesStringBuilder = new StringBuilder();
            for (OrderDetail orderDetail : orderDetailList) {
                // 份数
                Integer copies = orderDetail.getNumber();
                // 口味数据
                String dishFlavor = orderDetail.getDishFlavor();
                if (orderDetail.getDishId() != null) {
                    // 是个菜品
                    Dish dish = dishMapper.selectByPrimaryKey(orderDetail.getDishId());
                    orderDishesStringBuilder.append(dish.getName()).append("x").append(copies);
                    // 显示口味数据
                    if (dishFlavor != null && !dishFlavor.isEmpty()) {
                        orderDishesStringBuilder.append("(").append(dishFlavor).append("),");
                    } else {
                        orderDishesStringBuilder.append(",");
                    }
                } else if (orderDetail.getSetmealId() != null) {
                    // 是个套餐
                    Setmeal setmeal = setmealMapper.selectByPrimaryKey(orderDetail.getSetmealId());
                    orderDishesStringBuilder.append(setmeal.getName()).append("x").append(copies).append(",");
                } else {
                    // 啥也不是
                    log.error("订单详情异常: {}", orderDetail);
                }
            }
            OrderWithDishesStrVO orderWithDishesStrVO = objectMapper.convertValue(order, OrderWithDishesStrVO.class);
            if (!orderDishesStringBuilder.isEmpty()) {
                orderDishesStringBuilder.deleteCharAt(orderDishesStringBuilder.length() - 1);
            }
            orderWithDishesStrVO.setOrderDishes(orderDishesStringBuilder.toString());
            orderWithDishesStrVOList.add(orderWithDishesStrVO);
        }
        return Page.builder()
                .total(total)
                .records(orderWithDishesStrVOList)
                .build();
    }


     /**
     * 查询订单详情
     * @param id 订单id
     * @return OrderWithDetailsVO 订单详情
     */
    @Override
    public OrderWithDetailsVO queryOrderDetailsById(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.queryByOrderId(id);
        OrderWithDetailsVO orderWithDetails = objectMapper.convertValue(order, OrderWithDetailsVO.class);
        orderWithDetails.setOrderDetailList(orderDetailList);
        return orderWithDetails;
    }

    /**
     * 商家接单
     * @param id 订单id
     * @return int 受影响行数
     */
    @Override
    public int confirmOrder(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order == null) {
            return 0;
        }
        order.setStatus(Order.CONFIRMED);
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 商家取消订单
     * @param order 订单
     * @return int 受影响行数
     */
    @Override
    public int cancelOrder(Order order) {
        Order orderToBeCanceled = orderMapper.selectByPrimaryKey(order.getId());
        orderToBeCanceled.setCancelTime(LocalDateTime.now());
        orderToBeCanceled.setCancelReason(order.getCancelReason());
        orderToBeCanceled.setStatus(Order.CANCELLED);
        // TODO: 发起退款
        /*try {
            String refundResponseBody = weChatPayUtil.refund(order.getNumber(), order.getNumber(), order.getAmount(), order.getAmount());
            // TODO: 解析退款结果
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        orderToBeCanceled.setPayStatus(Order.REFUND);
        return orderMapper.updateByPrimaryKeySelective(orderToBeCanceled);
    }

    /**
     * 商家拒单
     * @param order 订单
     * @return int 受影响行数
     */
    @Override
    public int rejectOrder(Order order) {
        Order orderToBeRejected = orderMapper.selectByPrimaryKey(order.getId());
        orderToBeRejected.setRejectionReason(order.getRejectionReason());
        orderToBeRejected.setCancelTime(LocalDateTime.now());
        orderToBeRejected.setCancelReason("商家拒单");
        orderToBeRejected.setStatus(Order.CANCELLED);
        // TODO: 发起退款
        /*try {
            String refundResponseBody = weChatPayUtil.refund(order.getNumber(), order.getNumber(), order.getAmount(), order.getAmount());
            // TODO: 解析退款结果
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        orderToBeRejected.setPayStatus(Order.REFUND);
        return orderMapper.updateByPrimaryKeySelective(orderToBeRejected);
    }

    /**
     * 配送订单
     * @param id 订单id
     * @return int 受影响行数
     */
    @Override
    public int deliveryOrder(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        order.setStatus(Order.DELIVERY_IN_PROGRESS);
        order.setDeliveryTime(LocalDateTime.now());
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 完成订单
     * @param id 订单id
     * @return int 受影响行数
     */
    @Override
    public int completeOrder(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        order.setStatus(Order.COMPLETED);
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 统计订单数量
     * @return OrderStatisticsVO 订单统计数据
     */
    @Override
    public OrderStatisticsVO countOrderStatistics() {
        // 待接单
        Integer toBeConfirmed = orderMapper.countStatus(Order.TO_BE_CONFIRMED);
        // 待派送
        Integer confirmed = orderMapper.countStatus(Order.CONFIRMED);
        // 派送中
        Integer deliveryInProgress = orderMapper.countStatus(Order.DELIVERY_IN_PROGRESS);
        return OrderStatisticsVO.builder()
                .confirmed(confirmed)
                .deliveryInProgress(deliveryInProgress)
                .toBeConfirmed(toBeConfirmed)
                .build();
    }


}
