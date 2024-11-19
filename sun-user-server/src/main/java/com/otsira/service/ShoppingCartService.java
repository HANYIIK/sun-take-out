package com.otsira.service;

import com.otsira.dto.ShoppingCartDTO;
import com.otsira.entity.ShoppingCart;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 购物车相关的 service 层接口
 * @create: 2024/11/18 21:36
 */
public interface ShoppingCartService {
    List<ShoppingCart> queryShoppingCart();
    int insert(ShoppingCartDTO shoppingCartDTO);
    int delete(ShoppingCartDTO shoppingCartDTO);
    int deleteAll();
}
