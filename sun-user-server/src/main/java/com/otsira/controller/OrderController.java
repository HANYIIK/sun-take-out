package com.otsira.controller;

import com.otsira.dto.OrderSubmitDTO;
import com.otsira.dto.OrdersPaymentDTO;
import com.otsira.result.Page;
import com.otsira.result.Result;
import com.otsira.service.OrderService;
import com.otsira.util.UserContext;
import com.otsira.vo.OrderPaymentVO;
import com.otsira.vo.OrderSubmitVO;
import com.otsira.vo.OrderWithDetailsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户下单相关功能的 controller 层
 * @create: 2024/11/22 18:49
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户下单相关接口")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation("用户提交订单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO) {
        log.info("用户 id-{} 正在提交订单: {}", UserContext.getUserId(), orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(orderSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("用户历史订单的分页模糊查询")
    public Result<Page> historyOrders(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "status", required = false) Integer status) {
        log.info("用户 id-{} 正在查询历史订单", UserContext.getUserId());
        Page pageResult = orderService.historyOrders(page, pageSize, status);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("根据订单id查询订单详情")
    public Result<OrderWithDetailsVO> orderDetail(@PathVariable Long id) {
        log.info("用户 id-{} 正在查询订单 id-{} 的订单明细", UserContext.getUserId(), id);
        OrderWithDetailsVO orderWithDetailsVO = orderService.orderDetail(id);
        return Result.success(orderWithDetailsVO);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("用户取消订单")
    public Result<Object> cancelOrder(@PathVariable Long id) {
        log.info("用户 id-{} 正在取消订单 id-{}", UserContext.getUserId(), id);
        int update = orderService.cancelOrder(id);
        if (update <= 0) {
            return Result.error("用户取消订单失败");
        }
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result<Object> repetition(@PathVariable Long id) {
        log.info("用户 id-{} 正在再来一单 id-{}", UserContext.getUserId(), id);
        int insert = orderService.repeatOrder(id);
        if (insert <= 0) {
            return Result.error("再来一单失败");
        }
        return Result.success();
    }
}
