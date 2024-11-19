package com.otsira.mapper;

import com.otsira.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 购物车相关的 mapper 层接口
 * @create: 2024/11/18 21:36
 */
public interface ShoppingCartMapper extends Mapper<ShoppingCart> {

    @Select("select * from shopping_cart where user_id = #{userId} and dish_id = #{dishId}")
    ShoppingCart queryByUserIdAndDishId(Long userId, Long dishId);

    @Select("select * from shopping_cart where user_id = #{userId} and setmeal_id = #{setmealId}")
    ShoppingCart queryByUserIdAndSetmealId(Long userId, Long setmealId);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    int deleteByUserId(Long userId);
}
