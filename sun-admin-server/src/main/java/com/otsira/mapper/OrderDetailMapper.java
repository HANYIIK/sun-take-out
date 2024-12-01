package com.otsira.mapper;

import com.otsira.entity.OrderDetail;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

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
}
