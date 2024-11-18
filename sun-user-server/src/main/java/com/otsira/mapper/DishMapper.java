package com.otsira.mapper;

import com.otsira.entity.Dish;
import com.otsira.entity.DishFlavor;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品管理相关的 mapper 层接口
 * @create: 2024/11/16 20:17
 */
public interface DishMapper extends Mapper<Dish> {
    @Select("select * from dish where category_id = #{categoryId} and status = 1 order by update_time desc")
    List<Dish> queryDishesByCategoryId(Integer categoryId);

    @Select("SELECT dish_flavor.* FROM dish, dish_flavor WHERE dish.`id`=dish_flavor.`dish_id` AND dish_flavor.`dish_id`=#{dishId}")
    List<DishFlavor> queryFlavorByDishId(Long dishId);
}
