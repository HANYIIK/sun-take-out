package com.otsira.controller;

import com.otsira.result.Result;
import com.otsira.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 购物车相关的 controller 层
 * @create: 2024/11/16 20:29
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车相关接口")
public class ShoppingCartController {
    private static final String REDIS_KEY = "shoppingCart";
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        log.info("用户端购物车 Controller 层正在注入 RedisTemplate: {}", redisTemplate);
        this.redisTemplate = redisTemplate;
    }

    /*@ApiOperation("查询购物车")
    @GetMapping("/list")
    public Result<Object> queryShoppingCart() {
        log.info("用户 id-{} 正在请求获取购物车", UserContext.getUserId());
        // TODO: 2024/11/16 20:31 查询购物车
        redisTemplate.opsForValue();
        return Result.success();
    }*/
}
