package com.otsira.controller;

import com.otsira.dto.ShoppingCartDTO;
import com.otsira.entity.ShoppingCart;
import com.otsira.result.Result;
import com.otsira.service.ShoppingCartService;
import com.otsira.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 购物车相关的 controller 层
 * @create: 2024/11/16 20:29
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车相关接口")
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @ApiOperation("查询购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> queryShoppingCart() {
        log.info("用户 id-{} 正在请求获取购物车", UserContext.getUserId());
        List<ShoppingCart> shoppingCarts = shoppingCartService.queryShoppingCart();
        return Result.success(shoppingCarts);
    }

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result<Object> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("用户 id-{} 正在请求在购物车中添加菜品或套餐: {}", UserContext.getUserId(), shoppingCartDTO);
        int insert = shoppingCartService.insert(shoppingCartDTO);
        if (insert <= 0) {
            return Result.error("添加购物车失败");
        }
        return Result.success();
    }

    @ApiOperation("删除购物车")
    @PostMapping("/sub")
    public Result<Object> subShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("用户 id-{} 正在请求在购物车中删除菜品或套餐: {}", UserContext.getUserId(), shoppingCartDTO);
        int delete = shoppingCartService.delete(shoppingCartDTO);
        if (delete <= 0) {
            return Result.error("删除购物车失败");
        }
        return Result.success();
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result<Object> cleanShoppingCart() {
        log.info("用户 id-{} 正在请求清空购物车", UserContext.getUserId());
        int delete = shoppingCartService.deleteAll();
        if (delete <= 0) {
            return Result.error("清空购物车失败");
        }
        return Result.success();
    }
}
