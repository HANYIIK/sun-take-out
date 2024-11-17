package com.otsira.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.entity.Dish;
import com.otsira.mapper.DishMapper;
import com.otsira.service.DishService;
import com.otsira.vo.DishWithFlavorsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class DefaultDishService implements DishService {
    private DishMapper dishMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public void setDishMapper(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 根据菜品的分类 id 查询该类别下的所有菜品(需要包含口味数据)
     * @param categoryId 菜品的分类 id
     * @return 菜品列表
     */
    @Override
    public List<DishWithFlavorsVO> queryDishesByCategoryId(Integer categoryId) {
        List<Dish> dishes = dishMapper.queryDishesByCategoryId(categoryId);
        ArrayList<DishWithFlavorsVO> dishesWithFlavors = new ArrayList<>();
        for (Dish dish : dishes) {
            DishWithFlavorsVO dishWithFlavorsVO = objectMapper.convertValue(dish, DishWithFlavorsVO.class);
            dishWithFlavorsVO.setFlavors(dishMapper.queryFlavorByDishId(dish.getId()));
            dishesWithFlavors.add(dishWithFlavorsVO);
        }
        return dishesWithFlavors;
    }
}
