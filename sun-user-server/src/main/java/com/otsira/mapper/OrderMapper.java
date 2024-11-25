package com.otsira.mapper;

import com.otsira.entity.Order;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户下单相关功能的 mapper 层接口
 * @create: 2024/11/22 18:54
 */
public interface OrderMapper extends Mapper<Order> {
    /**
     * 根据订单号查询订单
     * @param outTradeNo 订单号
     * @return Order 订单对象
     */
    @Select("select * from orders where number = #{outTradeNo}")
    Order queryByNumber(String outTradeNo);
}
