package com.otsira.controller;

import com.otsira.entity.Order;
import com.otsira.result.Page;
import com.otsira.result.Result;
import com.otsira.service.OrderService;
import com.otsira.vo.OrderStatisticsVO;
import com.otsira.vo.OrderWithDetailsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 订单相关功能的 controller 层
 * @create: 2024/11/29 17:23
 */
@RestController
@Slf4j
@Api(tags = "订单功能相关接口")
@RequestMapping("/admin/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation("条件查询订单")
    @GetMapping("/conditionSearch")
    public Result<Page> queryAllOrders(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       // 订单号
                                       @RequestParam(value = "number", required = false) String number,
                                        // 用户手机号
                                       @RequestParam(value = "phone", required = false) String phone,
                                        // 下单时间范围: 起始
                                       @RequestParam(value = "beginTime", required = false) String beginTime,
                                        // 下单时间范围: 结束
                                       @RequestParam(value = "endTime", required = false) String endTime,
                                       // 订单状态
                                       @RequestParam(value = "status", required = false) String status) {

        return Result.success(orderService.queryAllOrders(page, pageSize, number, phone, beginTime, endTime, status));
    }


    @ApiOperation("查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderWithDetailsVO> queryOrderDetails(@PathVariable(value = "id") Long id) {
        return Result.success(orderService.queryOrderDetailsById(id));
    }

    @ApiOperation("商家接单")
    @PutMapping("/confirm")
    public Result<Object> confirmOrder(@RequestBody Order order) {
        int confirm = orderService.confirmOrder(order.getId());
        if (confirm <= 0) {
            return Result.error("接单失败");
        }
        return Result.success();
    }

    @ApiOperation("商家拒单")
    @PutMapping("/rejection")
    public Result<Object> rejectOrder(@RequestBody Order order) {
        int reject = orderService.rejectOrder(order);
        if (reject <= 0) {
            return Result.error("拒单失败");
        }
        return Result.success();
    }

    @ApiOperation("商家取消订单")
    @PutMapping("/cancel")
    public Result<Object> cancelOrder(@RequestBody Order order) {
        int confirm = orderService.cancelOrder(order);
        if (confirm <= 0) {
            return Result.error("拒单失败");
        }
        return Result.success();
    }

    @ApiOperation("配送订单")
    @PutMapping("/delivery/{id}")
    public Result<Object> deliveryOrder(@PathVariable(value = "id") Long id) {
        int delivery = orderService.deliveryOrder(id);
        if (delivery <= 0) {
            return Result.error("配送失败");
        }
        return Result.success();
    }

    @ApiOperation("完成订单")
    @PutMapping("/complete/{id}")
    public Result<Object> completeOrder(@PathVariable(value = "id") Long id) {
        int complete = orderService.completeOrder(id);
        if (complete <= 0) {
            return Result.error("订单完成失败");
        }
        return Result.success();
    }

    @ApiOperation("各个状态的订单数量统计")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> orderStatistics() {
        return Result.success(orderService.countOrderStatistics());
    }

}
