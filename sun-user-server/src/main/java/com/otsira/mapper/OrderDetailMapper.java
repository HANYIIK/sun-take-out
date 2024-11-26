package com.otsira.mapper;

import com.otsira.entity.OrderDetail;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户订单详情相关功能的 mapper 层接口
 * @create: 2024/11/22 18:58
 */
public interface OrderDetailMapper extends Mapper<OrderDetail> {
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> queryOrderDetailByOrderId(Long id);
}
