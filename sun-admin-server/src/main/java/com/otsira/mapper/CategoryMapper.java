package com.otsira.mapper;

import com.otsira.entity.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品分类管理的 mapper 层接口
 * @create: 2024/10/30 17:01
 */
public interface CategoryMapper extends Mapper<Category> {
    @Select("select * from category where name like concat('%',#{name},'%') and type like concat('%',#{type},'%') ORDER BY sort LIMIT #{start},#{pageSize}")
    List<Category> queryPage(Integer start, Integer pageSize, String name, String type);

    @Select("select count(*) from category where name like concat('%',#{name},'%') and type like concat('%',#{type},'%')")
    int queryPageCount(String name, String type);

    @Select("select * from category where name = #{name}")
    Category selectByName(String name);

    @Select("select * from category where type = #{type}")
    List<Category> queryByType(Integer type);
}
