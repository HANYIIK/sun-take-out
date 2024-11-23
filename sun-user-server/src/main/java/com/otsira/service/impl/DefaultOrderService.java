package com.otsira.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.constant.MessageConstant;
import com.otsira.dto.OrderSubmitDTO;
import com.otsira.entity.*;
import com.otsira.exception.AddressBookBusinessException;
import com.otsira.exception.ShoppingCartBusinessException;
import com.otsira.mapper.*;
import com.otsira.service.OrderService;
import com.otsira.util.UserContext;
import com.otsira.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户下单相关功能的 service 接口实现类
 * @create: 2024/11/22 18:53
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class DefaultOrderService implements OrderService {
    private OrderMapper orderMapper;
    private OrderDetailMapper orderDetailMapper;
    private AddressBookMapper addressBookMapper;
    private ShoppingCartMapper shoppingCartMapper;
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
    public void setAddressBookMapper(AddressBookMapper addressBookMapper) {
        this.addressBookMapper = addressBookMapper;
    }

    @Autowired
    public void setShoppingCartMapper(ShoppingCartMapper shoppingCartMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrderSubmitDTO orderSubmitDTO) {
        // 1.处理业务异常（购物车为空或收货地址为空, 不能下单）
        // 1.1 地址簿不能为空
        AddressBook addressBook = addressBookMapper.selectByPrimaryKey(orderSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            log.error("用户 id-{} 提交订单失败: 收货地址为空", UserContext.getUserId());
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 1.2 购物车能为空
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.queryByUserId(UserContext.getUserId());
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            log.error("用户 id-{} 提交订单失败: 购物车为空", UserContext.getUserId());
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 2.没有业务异常, 正常下单
        Order order = objectMapper.convertValue(orderSubmitDTO, Order.class);

        // 2.1 设置订单号
        // Generate a unique numeric order number using timestamp and random number
        String uniqueOrderNumber = String.valueOf(Instant.now().toEpochMilli()) + new Random().nextInt(1000);
        order.setNumber(uniqueOrderNumber);

        // 2.2 设置订单状态: 1-待付款
        order.setStatus(Order.PENDING_PAYMENT);

        // 2.3 下单用户的 userId
        order.setUserId(UserContext.getUserId());

        // 2.4 下单时间: now
        order.setOrderTime(LocalDateTime.now());

        // 2.5 支付状态: 0-未支付
        order.setPayStatus(Order.UN_PAID);

        // 2.6 用户名
        order.setUserName(addressBook.getConsignee());

        // 2.7 手机号
        order.setPhone(addressBook.getPhone());

        // 2.8 地址
        order.setAddress(addressBook.getProvinceName()
                + addressBook.getCityName()
                + addressBook.getDistrictName()
                + addressBook.getDetail());

        // 2.9 收货人
        order.setConsignee(addressBook.getConsignee());

        // 3.插入 order 表
        int insert = orderMapper.insertSelective(order);
        if (insert <= 0) {
            log.error("用户 id-{} 提交订单失败: {}", UserContext.getUserId(), order);
            throw new RuntimeException("提交订单失败");
        }

        // 4.插入 orderDetail
        int insertOrderDetail = 0;
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .name(shoppingCart.getName())
                    .orderId(order.getId())
                    .dishId(shoppingCart.getDishId())
                    .setmealId(shoppingCart.getSetmealId())
                    .dishFlavor(shoppingCart.getDishFlavor())
                    .number(shoppingCart.getNumber())
                    .amount(shoppingCart.getAmount())
                    .image(shoppingCart.getImage())
                    .build();
            insertOrderDetail += orderDetailMapper.insertSelective(orderDetail);
        }

        // 5.清空购物车
        if (insertOrderDetail > 0) {
            shoppingCartMapper.deleteByUserId(UserContext.getUserId());
        }

        // 6.返回订单提交成功的 VO
        return OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();
    }
}
