package com.otsira.service;

import com.otsira.vo.DishWithFlavorsVO;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户端的 service 层接口
 * @create: 2024/10/26 16:59
 */
public interface DishService {
    List<DishWithFlavorsVO> queryDishesByCategoryId(Integer categoryId);
}
