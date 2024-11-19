package com.otsira.service.impl;

import com.otsira.entity.Setmeal;
import com.otsira.mapper.SetmealMapper;
import com.otsira.service.SetmealService;
import com.otsira.vo.DishWithCopiesVO;
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
public class DefaultSetmealService implements SetmealService {
    private SetmealMapper setmealMapper;

    @Autowired
    public void setSetmealMapper(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    /**
     * 根据套餐的分类 id 查询所有该类别下的所有套餐
     * @param categoryId 套餐的分类 id
     * @return 套餐列表
     */
    @Override
    public List<Setmeal> querySetmealsByCategoryId(Integer categoryId) {
        return setmealMapper.querySetmealsByCategoryId(categoryId);
    }

    /**
     * 根据套餐 id 查询该套餐下的所有菜品(需要带每个菜品的份数)
     * @param id 套餐 id
     * @return 套餐下的所有菜品(需要带每个菜品的份数)
     */
    @Override
    public List<DishWithCopiesVO> querySetmealDishesById(Long id) {
        return setmealMapper.querySetmealDishesById(id);
    }

}
