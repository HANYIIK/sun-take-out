package com.otsira.service;

import com.otsira.entity.Setmeal;
import com.otsira.vo.DishWithCopiesVO;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户端的 service 层接口
 * @create: 2024/10/26 16:59
 */
public interface SetmealService {
    List<Setmeal> querySetmealsByCategoryId(Integer categoryId);
    List<DishWithCopiesVO> querySetmealDishesById(Long id);
}
