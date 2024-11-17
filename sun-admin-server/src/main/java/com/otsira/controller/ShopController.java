package com.otsira.controller;

import com.otsira.constant.StatusConstant;
import com.otsira.result.Result;
import com.otsira.util.EmployeeContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 店铺营业状态相关的 controller 层
 * @create: 2024/11/10 16:02
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@RestController("adminShopController")
@Slf4j
@Api(tags = "店铺营业状态相关接口")
@RequestMapping("/admin/shop")
public class ShopController {
    private static final String REDIS_KEY = "shopStatus";
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        log.info("管理端正在注入 RedisTemplate: {}", redisTemplate);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置店铺营业状态
     * @param status 0: 打烊 1: 营业
     * @return 操作结果
     */
    @ApiOperation("设置店铺营业状态")
    @PutMapping("/{status}")
    public Result<Object> setShopStatus(@PathVariable("status") Integer status) {
        log.info("管理员 id-{} 请求设置店铺营业状态为: {}", EmployeeContext.getEmpId(), status.equals(StatusConstant.ENABLE) ? "营业" : "打烊");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(REDIS_KEY, status);
        return Result.success();
    }

    /**
     * 获取店铺营业状态
     * @return 带有店铺营业状态的操作结果
     */
    @ApiOperation("获取店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        log.info("管理员 id-{} 请求获取店铺营业状态", EmployeeContext.getEmpId());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(REDIS_KEY);
        return Result.success(status);
    }
}
