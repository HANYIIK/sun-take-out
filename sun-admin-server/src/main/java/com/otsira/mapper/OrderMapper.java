package com.otsira.mapper;

import com.otsira.entity.Order;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 订单相关功能的 mapper 层接口
 * @create: 2024/11/29 17:39
 */
public interface OrderMapper extends Mapper<Order> {
    @Select("SELECT COUNT(*) FROM orders WHERE number LIKE CONCAT('%',#{number},'%') AND phone LIKE CONCAT('%',#{phone},'%') AND order_time BETWEEN #{beginTime} AND #{endTime} AND status LIKE CONCAT('%',#{status},'%')")
    Integer queryPageCount(String number, String phone, String beginTime, String endTime, String status);

    @Select("SELECT * FROM orders WHERE number LIKE CONCAT('%',#{number},'%') AND phone LIKE CONCAT('%',#{phone},'%') AND order_time BETWEEN #{beginTime} AND #{endTime} AND status LIKE CONCAT('%',#{status},'%') order by order_time desc LIMIT #{start},#{pageSize}")
    List<Order> queryOrderByPage(Integer start, Integer pageSize, String number, String phone, String beginTime, String endTime, String status);

    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Integer countStatus(Integer status);

    @Select("SELECT COUNT(*) FROM orders")
    Integer countAllOrders();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 6")
    Integer countCancelledOrders();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 5")
    Integer countCompletedOrders();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 3")
    Integer countDeliveredOrders();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 2")
    Integer countWaitingOrders();

    @Select("SELECT SUM(amount) FROM orders WHERE status = #{status} AND order_time BETWEEN #{beginTime} AND #{endTime}")
    Double sumTurnoverByDate(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    @Select("SELECT COUNT(*) FROM orders WHERE order_time BETWEEN #{beginTime} AND #{endTime}")
    Integer countAllOrdersByDate(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("SELECT COUNT(*) FROM orders WHERE order_time BETWEEN #{beginTime} AND #{endTime} AND status = #{status}")
    Integer countValidOrderByDate(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Integer countValidOrders(Integer status);

    @Select("SELECT SUM(amount) FROM orders WHERE status = 5 AND order_time BETWEEN #{begin} AND #{end}")
    Double countTurnoverByDate(LocalDateTime begin, LocalDateTime end);
}
