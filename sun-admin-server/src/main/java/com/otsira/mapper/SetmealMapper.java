package com.otsira.mapper;

import com.otsira.entity.Setmeal;
import com.otsira.entity.SetmealDish;
import com.otsira.vo.SetmealInfoVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 套餐管理的 mapper 层接口
 * @create: 2024/11/04 20:07
 */
public interface SetmealMapper extends Mapper<Setmeal> {
    @Select("select count(*) from setmeal where name like concat('%',#{name},'%') and category_id like concat('%',#{categoryId},'%') and status like concat('%',#{status},'%')")
    int queryPageCount(String name, String categoryId, String status);

    @Select("select setmeal.*, category.name as category_name from setmeal, category where setmeal.category_id = category.id and setmeal.name like concat('%',#{name},'%') and setmeal.category_id like concat('%',#{categoryId},'%') and setmeal.status like concat('%',#{status},'%') limit #{start},#{pageSize}")
    List<SetmealInfoVO> queryPage(Integer start, Integer pageSize, String name, String categoryId, String status);

    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies) values (#{setmealId}, #{dishId}, #{name}, #{price}, #{copies})")
    void insertSetmealDish(SetmealDish setmealDish);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> querySetmealDishesBySetmealId(Long id);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteSetmealDishesBySetmealId(Long id);

    @Select("select * from setmeal_dish where dish_id = #{id}")
    List<SetmealDish> querySetmealDishesByDishId(Long id);

    @Select("select * from setmeal where category_id = #{id}")
    List<Setmeal> querySetmealByCategoryId(Long id);
}
