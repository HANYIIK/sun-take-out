package com.otsira.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.constant.MessageConstant;
import com.otsira.dto.OrderSubmitDTO;
import com.otsira.dto.OrdersPaymentDTO;
import com.otsira.entity.*;
import com.otsira.exception.AddressBookBusinessException;
import com.otsira.exception.ShoppingCartBusinessException;
import com.otsira.mapper.*;
import com.otsira.result.Page;
import com.otsira.service.OrderService;
import com.otsira.util.UserContext;
import com.otsira.vo.OrderPaymentVO;
import com.otsira.vo.OrderSubmitVO;
import com.otsira.vo.OrderWithDetailsVO;
import com.otsira.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    // private UserMapper userMapper;
    private ObjectMapper objectMapper;
    // private WeChatPayUtil weChatPayUtil;
    private WebSocketServer webSocketServer;

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

    // @Autowired
    // public void setUserMapper(UserMapper userMapper) {
    //     this.userMapper = userMapper;
    // }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // @Autowired
    // public void setWeChatPayUtil(WeChatPayUtil weChatPayUtil) {
    //     this.weChatPayUtil = weChatPayUtil;
    // }

    @Autowired
    public void setWebSocketServer(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    /**
     * 用户提交订单
     * @param orderSubmitDTO 订单提交的 DTO
     * @return OrderSubmitVO 订单提交成功的 VO
     */
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

    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付的 DTO
     * @return OrderPaymentVO 订单支付成功的 VO
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) /*throws Exception*/ {
        /*// 当前登录用户
        User user = userMapper.selectByPrimaryKey(UserContext.getUserId());

        // 调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                // 商户订单号
                ordersPaymentDTO.getOrderNumber(),
                // 支付金额，单位: 元
                new BigDecimal("0.01"),
                // 商品描述
                "苍穹外卖订单",
                // 微信用户的 openid
                user.getOpenid()
        );

        if (jsonObject.getString("code") != null && "ORDERPAID".equals(jsonObject.getString("code"))) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        // packageStr = "prepay_id=xxxxxxxxx"
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;*/

        // 【没有商户, 跳过微信支付】
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return new OrderPaymentVO();
    }

    /**
     * 支付成功, 修改 orders 表: 订单的状态、支付方式、支付状态、结账时间
     * @param outTradeNo 商户订单号
     * @return 受影响行数
     */
    @Override
    public int paySuccess(String outTradeNo) {
        Order order = Order.builder()
                // 订单 id
                .id(orderMapper.queryByNumber(outTradeNo).getId())
                // 修改订单状态为: 2-待接单
                .status(Order.TO_BE_CONFIRMED)
                // 修改支付状态为: 1-已支付
                .payStatus(Order.PAID)
                // 结账时间: now
                .checkoutTime(LocalDateTime.now())
                .build();

        // 支付成功, 向商家推送消息
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("orderId", order.getId());
        map.put("content", "订单号: " + outTradeNo);
        String jsonString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);

        return orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 查询历史订单
     * @param page 当前页码
     * @param pageSize 一页几条数据
     * @param status 订单状态
     * @return Page<OrderWithDetailsVO> 分页数据
     */
    @Override
    public Page historyOrders(Integer page, Integer pageSize, Integer status) {
        String statusStr = status == null ? "" : status.toString();
        int total = orderMapper.queryPageCountByUserId(UserContext.getUserId(), statusStr);
        Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer start = (page - 1) * pageSize;
        List<OrderWithDetailsVO> orderWithDetailsList = orderMapper.queryOrderWithDetailsAsPage(UserContext.getUserId(), statusStr, start, pageSize);
        for (OrderWithDetailsVO orderWithDetails : orderWithDetailsList) {
            orderWithDetails.setOrderDetailList(orderDetailMapper.queryOrderDetailByOrderId(orderWithDetails.getId()));
        }
        return Page.builder()
                .total(total)
                .records(orderWithDetailsList)
                .build();
    }

    /**
     * 根据订单 id 查询包含订单详情的订单信息
     * @param id 订单 id
     * @return OrderWithDetailsVO 包含订单详情的订单信息
     */
    @Override
    public OrderWithDetailsVO orderDetail(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        OrderWithDetailsVO orderWithDetails = objectMapper.convertValue(order, OrderWithDetailsVO.class);
        orderWithDetails.setOrderDetailList(orderDetailMapper.queryOrderDetailByOrderId(id));
        return orderWithDetails;
    }

    /**
     * 用户取消订单
     * @param id 订单 id
     * @return 受影响行数
     */
    @Override
    public int cancelOrder(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        order.setStatus(Order.CANCELLED);
        order.setCancelReason("用户方取消订单");
        order.setCancelTime(LocalDateTime.now());
        // TODO: 用户退款
        /*try {
            String refundResponseBody = weChatPayUtil.refund(order.getNumber(), order.getNumber(), order.getAmount(), order.getAmount());
            // TODO: 解析退款结果
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        order.setPayStatus(Order.REFUND);
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 再来一单
     * @param id 订单 id
     * @return 受影响行数
     */
    @Override
    public int repeatOrder(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        // 设置订单 id
        order.setId(null);

        // 设置新订单号
        String uniqueOrderNumber = String.valueOf(Instant.now().toEpochMilli()) + new Random().nextInt(1000);
        order.setNumber(uniqueOrderNumber);

        // 设置订单状态: 1-待付款
        order.setStatus(Order.PENDING_PAYMENT);

        // 下单时间: now
        order.setOrderTime(LocalDateTime.now());

        // 结账时间: null
        order.setCheckoutTime(null);

        // 取消订单原因
        order.setCancelReason(null);

        // 取消订单时间
        order.setCancelTime(null);

        // 预计送达时间
        order.setEstimatedDeliveryTime(null);

        // 支付状态: 0-未支付
        order.setPayStatus(Order.UN_PAID);

        // 插入 order 表
        int insert = orderMapper.insertSelective(order);
        if (insert <= 0) {
            log.error("用户 id-{} 再来一单失败: {}", UserContext.getUserId(), order);
            throw new RuntimeException("再来一单失败");
        }

        // 插入 orderDetail 表
        List<OrderDetail> orderDetails = orderDetailMapper.queryOrderDetailByOrderId(id);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setId(null);
            orderDetail.setOrderId(order.getId());
            insert += orderDetailMapper.insertSelective(orderDetail);
        }
        return insert;
    }

    /**
     * 用户催单
     * @param id 订单 id
     * @return 催单成功/失败
     */
    @Override
    public boolean reminder(Long id) {
        try {
            Order order = orderMapper.selectByPrimaryKey(id);
            HashMap<String, Object> map = new HashMap<>();
            // type 1-来单提醒
            // type 2-用户催单
            map.put("type", 2);
            map.put("orderId", order.getId());
            map.put("content", order.getNumber());
            String jsonString = JSON.toJSONString(map);
            webSocketServer.sendToAllClient(jsonString);
        } catch (Exception e) {
            log.info("给客户端发送消息失败: {}", e.getMessage());
            return false;
        }
        return true;
    }
}
