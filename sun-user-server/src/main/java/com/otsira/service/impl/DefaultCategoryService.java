package com.otsira.service.impl;

import com.otsira.entity.Category;
import com.otsira.mapper.CategoryMapper;
import com.otsira.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户 service 层接口的默认实现类
 * @create: 2024/10/26 16:59
 */
@Service
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class DefaultCategoryService implements CategoryService {
    private CategoryMapper categoryMapper;

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    /**
     * 根据分类类型(套餐类/菜品类)模糊查询所有菜品分类
     * @param type 分类类型(1-菜品分类, 2-套餐分类)
     * @return 菜品分类/套餐列表
     */
    @Override
    public List<Category> queryCategoriesByType(String type) {
        if (type == null) {
            type = "";
        }
        return this.categoryMapper.queryCategoriesByType(type);
    }
}
