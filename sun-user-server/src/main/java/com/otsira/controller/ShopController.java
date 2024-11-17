package com.otsira.controller;

import com.otsira.constant.StatusConstant;
import com.otsira.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 店铺营业状态相关的 controller 层
 * @create: 2024/11/10 16:02
 */
@SuppressWarnings({"rawtypes"})
@RestController("userShopController")
@Slf4j
@Api(tags = "店铺营业状态相关接口")
@RequestMapping("/user/shop")
public class ShopController {
    private static final String REDIS_KEY = "shopStatus";
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        log.info("用户端店铺状态 Controller 层正在注入 RedisTemplate: {}", redisTemplate);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取店铺营业状态
     * @return 带有店铺营业状态的操作结果
     */
    @ApiOperation("获取店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        log.info("用户请求获取店铺营业状态");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(REDIS_KEY);
        log.info("店铺营业状态为: {}", Objects.requireNonNull(status).equals(StatusConstant.ENABLE) ? "营业中" : "休息中");
        return Result.success(status);
    }
}
