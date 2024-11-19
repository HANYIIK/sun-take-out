package com.otsira.service.impl;

import com.otsira.dto.ShoppingCartDTO;
import com.otsira.entity.Dish;
import com.otsira.entity.Setmeal;
import com.otsira.entity.ShoppingCart;
import com.otsira.mapper.DishMapper;
import com.otsira.mapper.SetmealMapper;
import com.otsira.mapper.ShoppingCartMapper;
import com.otsira.service.ShoppingCartService;
import com.otsira.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 购物车相关的 service 层接口实现类
 * @create: 2024/11/18 21:38
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class DefaultShoppingCartService implements ShoppingCartService {
    private ShoppingCartMapper shoppingCartMapper;
    private DishMapper dishMapper;
    private SetmealMapper setmealMapper;

    @Autowired
    public void setShoppingCartMapper(ShoppingCartMapper shoppingCartMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
    }

    @Autowired
    public void setDishMapper(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Autowired
    public void setSetmealMapper(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    @Override
    public int insert(ShoppingCartDTO shoppingCartDTO) {
        int insert;
        // 1.是个菜品
        if (shoppingCartDTO.getDishId() != null) {
            // 不是该用户第一次添加该菜品
            ShoppingCart shoppingCartExist = shoppingCartMapper.queryByUserIdAndDishId(UserContext.getUserId(), shoppingCartDTO.getDishId());
            if (shoppingCartExist != null) {
                shoppingCartExist.setNumber(shoppingCartExist.getNumber() + 1);
                insert = shoppingCartMapper.updateByPrimaryKeySelective(shoppingCartExist);
                return insert;
            }
            Dish dish = dishMapper.selectByPrimaryKey(shoppingCartDTO.getDishId());
            ShoppingCart shoppingCart = ShoppingCart.builder()
                    .name(dish.getName())
                    .userId(UserContext.getUserId())
                    .dishId(dish.getId())
                    .dishFlavor(shoppingCartDTO.getDishFlavor())
                    .number(1)
                    .amount(dish.getPrice())
                    .image(dish.getImage())
                    .createTime(LocalDateTime.now())
                    .build();
            insert = shoppingCartMapper.insert(shoppingCart);
        // 是个套餐
        } else if (shoppingCartDTO.getSetmealId() != null) {
            // 不是该用户第一次添加该套餐
            ShoppingCart shoppingCartExist = shoppingCartMapper.queryByUserIdAndSetmealId(UserContext.getUserId(), shoppingCartDTO.getSetmealId());
            if (shoppingCartExist != null) {
                shoppingCartExist.setNumber(shoppingCartExist.getNumber() + 1);
                insert = shoppingCartMapper.updateByPrimaryKeySelective(shoppingCartExist);
                return insert;
            }
            Setmeal setmeal = setmealMapper.selectByPrimaryKey(shoppingCartDTO.getSetmealId());
            ShoppingCart shoppingCart = ShoppingCart.builder()
                    .name(setmeal.getName())
                    .userId(UserContext.getUserId())
                    .setmealId(setmeal.getId())
                    .number(1)
                    .amount(setmeal.getPrice())
                    .image(setmeal.getImage())
                    .createTime(LocalDateTime.now())
                    .build();
            insert = shoppingCartMapper.insert(shoppingCart);
        } else {
            // 既不是菜品也不是套餐
            throw new RuntimeException("购物车中的菜品或套餐 id 不能同时为空");
        }
        return insert;
    }

    @Override
    public int delete(ShoppingCartDTO shoppingCartDTO) {
        int delete;
        ShoppingCart shoppingCartExist;
        // 1.是个菜品
        if (shoppingCartDTO.getDishId() != null) {
            shoppingCartExist = shoppingCartMapper.queryByUserIdAndDishId(UserContext.getUserId(), shoppingCartDTO.getDishId());
        // 2.是个套餐
        } else if (shoppingCartDTO.getSetmealId() != null) {
            shoppingCartExist = shoppingCartMapper.queryByUserIdAndSetmealId(UserContext.getUserId(), shoppingCartDTO.getSetmealId());
        // 3.既不是菜品也不是套餐
        } else {
            throw new RuntimeException("购物车中的菜品或套餐 id 不能同时为空");
        }

        if (shoppingCartExist == null) {
            return 0;
        }
        if (shoppingCartExist.getNumber() > 1) {
            shoppingCartExist.setNumber(shoppingCartExist.getNumber() - 1);
            delete = shoppingCartMapper.updateByPrimaryKeySelective(shoppingCartExist);
        } else if (shoppingCartExist.getNumber() == 1) {
            delete = shoppingCartMapper.deleteByPrimaryKey(shoppingCartExist.getId());
        } else {
            throw new RuntimeException("购物车中的菜品数量不能小于 1");
        }

        return delete;
    }

    @Override
    public int deleteAll() {
        return shoppingCartMapper.deleteByUserId(UserContext.getUserId());
    }

    @Override
    public List<ShoppingCart> queryShoppingCart() {
        return shoppingCartMapper.selectAll();
    }
}
