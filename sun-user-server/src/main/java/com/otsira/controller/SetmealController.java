package com.otsira.controller;

import com.otsira.entity.Setmeal;
import com.otsira.result.Result;
import com.otsira.service.SetmealService;
import com.otsira.util.UserContext;
import com.otsira.vo.DishWithCopiesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户端套餐相关 controller 层接口
 * @create: 2024/11/16 15:56
 */
@RestController
@RequestMapping("/user/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {
    private SetmealService setmealService;

    @Autowired
    public void setSetmealService(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @GetMapping("/list")
    @ApiOperation("查询指定分类下的所有套餐")
    @Cacheable(value = "setmeal", key = "#categoryId")
    public Result<List<Setmeal>> list(@RequestParam("categoryId") Integer categoryId) {
        log.info("用户 id-{} 请求查询 categoryId-{} 下的所有套餐", UserContext.getUserId(), categoryId);
        List<Setmeal> setmeals = setmealService.querySetmealsByCategoryId(categoryId);
        return Result.success(setmeals);
    }

    @GetMapping("/dish/{id}")
    @ApiOperation("查询指定套餐下的所有菜品")
    public Result<List<DishWithCopiesVO>> querySetmealDishes(@PathVariable("id") Long id) {
        log.info("用户 id-{} 请求查询套餐 id-{} 下的所有菜品", UserContext.getUserId(), id);
        List<DishWithCopiesVO> setmealDishes = setmealService.querySetmealDishesById(id);
        return Result.success(setmealDishes);
    }
}
