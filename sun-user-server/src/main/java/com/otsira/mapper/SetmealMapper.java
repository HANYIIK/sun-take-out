package com.otsira.mapper;

import com.otsira.entity.Setmeal;
import com.otsira.vo.DishWithCopiesVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 套餐管理相关的 mapper 层接口
 * @create: 2024/11/16 20:16
 */
public interface SetmealMapper extends Mapper<Setmeal> {
    @Select("select * from setmeal where category_id = #{categoryId} and status = 1 order by update_time desc")
    List<Setmeal> querySetmealsByCategoryId(Integer categoryId);

    @Select("select dish.*, setmeal_dish.copies from dish, setmeal_dish where dish.id = setmeal_dish.dish_id and setmeal_id = #{id}")
    List<DishWithCopiesVO> querySetmealDishesById(Long id);
}
