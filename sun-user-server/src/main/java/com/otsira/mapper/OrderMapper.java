package com.otsira.mapper;

import com.otsira.entity.Order;
import com.otsira.vo.OrderWithDetailsVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 根据用户 id 和订单状态(可有可无)查询历史订单的数量
     * @param userId 用户 id
     * @param status 订单状态
     * @return int 该用户的历史订单数量
     */
    @Select("select count(*) from orders where user_id = #{userId} and status like concat('%',#{status},'%')")
    int queryPageCountByUserId(Long userId, String status);

    @Select("select * from orders where user_id = #{userId} and status like concat('%',#{status},'%') order by order_time desc limit #{start},#{pageSize}")
    List<OrderWithDetailsVO> queryOrderWithDetailsAsPage(Long userId, String status, Integer start, Integer pageSize);

    @Select("select * from orders where status = #{status} and order_time < #{timeoutTime}")
    List<Order> queryByStatusAndOrderTime(Integer status, LocalDateTime timeoutTime);
}
