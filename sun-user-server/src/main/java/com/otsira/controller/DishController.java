package com.otsira.controller;

import com.otsira.result.Result;
import com.otsira.service.DishService;
import com.otsira.util.UserContext;
import com.otsira.vo.DishWithFlavorsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品管理的 controller 层接口
 * @create: 2024/11/16 16:11
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "菜品管理相关接口")
public class DishController {
    private DishService dishService;
    private RedisTemplate redisTemplate;

    @Autowired
    public void setUserService(DishService userService) {
        this.dishService = userService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        log.info("用户端菜品管理 Controller 层正在注入 RedisTemplate: {}", redisTemplate);
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/list")
    @ApiOperation("查询某一菜品分类下的所有菜品")
    public Result<List<DishWithFlavorsVO>> list(@RequestParam("categoryId") Integer categoryId) {
        log.info("用户 id-{} 请求查询 categoryId-{} 下的所有菜品", UserContext.getUserId(), categoryId);
        // 查询 Redis 缓存中的菜品信息
        String redisKey = "dish_" + categoryId;
        ValueOperations operations = redisTemplate.opsForValue();
        List<DishWithFlavorsVO> dishes = (List<DishWithFlavorsVO>) operations.get(redisKey);
        if (dishes != null && !dishes.isEmpty()) {
            log.info("用户 id-{} 在 Redis 缓存中查询到了 categoryId-{} 下的所有菜品", UserContext.getUserId(), categoryId);
            return Result.success(dishes);
        }

        // Redis 中没有, 查询数据库
        log.info("用户 id-{} 在 Redis 缓存中没有查询到 categoryId-{} 下的所有菜品, 正在查询数据库并存入 Redis 缓存", UserContext.getUserId(), categoryId);
        dishes = dishService.queryDishesByCategoryId(categoryId);

        // 将从数据库中查询到的数据存入 Redis 缓存
        operations.set(redisKey, dishes);

        return Result.success(dishes);
    }
}
