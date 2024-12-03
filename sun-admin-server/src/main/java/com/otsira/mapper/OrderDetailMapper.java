package com.otsira.mapper;

import com.otsira.entity.OrderDetail;
import com.otsira.entity.Top10Order;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 订单详情相关功能的 mapper 层接口
 * @create: 2024/11/29 19:09
 */
public interface OrderDetailMapper extends Mapper<OrderDetail> {
    @Select("SELECT * FROM order_detail WHERE order_id = #{id}")
    List<OrderDetail> queryByOrderId(Long id);

    @Select("SELECT order_detail.name as name, SUM(order_detail.number) as total FROM order_detail, orders WHERE order_detail.order_id = orders.id AND orders.status = 5 AND orders.order_time BETWEEN #{beginTime} AND #{endTime} GROUP BY order_detail.name ORDER BY total DESC LIMIT 10")
    List<Top10Order> queryTop10(LocalDateTime beginTime, LocalDateTime endTime);
}
