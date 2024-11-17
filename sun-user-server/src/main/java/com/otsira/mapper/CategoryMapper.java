package com.otsira.mapper;

import com.otsira.entity.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品类别管理的 mapper 层接口
 * @create: 2024/11/16 20:15
 */
public interface CategoryMapper extends Mapper<Category> {
    @Select("select * from category where type like concat('%',#{type},'%') ORDER BY sort")
    List<Category> queryCategoriesByType(String type);
}
