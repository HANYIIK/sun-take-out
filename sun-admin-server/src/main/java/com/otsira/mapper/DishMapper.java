package com.otsira.mapper;

import com.otsira.entity.Dish;
import com.otsira.entity.DishFlavor;
import com.otsira.vo.DishWithFlavorsAndCategoryNameVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品管理的 mapper 层接口
 * @create: 2024/11/02 17:25
 */
public interface DishMapper extends Mapper<Dish> {
    @Select("select dish.*, category.name as category_name from dish, category where dish.category_id = category.id and dish.name like concat('%',#{name},'%') and dish.category_id like concat('%',#{categoryId},'%') and dish.status like concat('%',#{status},'%') ORDER BY dish.create_time desc LIMIT #{start},#{pageSize}")
    List<DishWithFlavorsAndCategoryNameVO> queryPage(Integer start, Integer pageSize, String name, String categoryId, String status);

    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> queryByCategoryId(Long categoryId);

    @Select("select count(*) from dish where name like concat('%',#{name},'%') and category_id like concat('%',#{categoryId},'%') and status like concat('%',#{status},'%')")
    int queryPageCount(String name, String categoryId, String status);

    @Select("SELECT dish_flavor.* FROM dish, dish_flavor WHERE dish.`id`=dish_flavor.`dish_id` AND dish_flavor.`dish_id`=#{dishId}")
    List<DishFlavor> queryFlavorByDishId(Long dishId);

    @Delete("DELETE FROM dish_flavor WHERE dish_id=#{id}")
    void deleteFlavorsByDishId(Long id);

    @Insert("INSERT INTO dish_flavor(dish_id, name, value) VALUES(#{dishId}, #{name}, #{value})")
    void insertFlavor(DishFlavor flavor);

    @Select("SELECT * FROM dish WHERE name=#{name}")
    Dish queryByName(String name);

    @Select("SELECT count(*) FROM dish WHERE status=1")
    Integer countSold();

    @Select("SELECT count(*) FROM dish WHERE status=0")
    Integer countDiscontinued();
}
