package com.otsira.controller;

import com.otsira.result.Result;
import com.otsira.service.DishService;
import com.otsira.util.UserContext;
import com.otsira.vo.DishWithFlavorsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "菜品管理相关接口")
public class DishController {
    private DishService dishService;

    @Autowired
    public void setUserService(DishService userService) {
        this.dishService = userService;
    }

    @GetMapping("/list")
    @ApiOperation("查询某一菜品分类下的所有菜品")
    public Result<List<DishWithFlavorsVO>> list(@RequestParam("categoryId") Integer categoryId) {
        log.info("用户 id-{} 请求查询 categoryId-{} 下的所有菜品", UserContext.getUserId(), categoryId);
        List<DishWithFlavorsVO> dishes = dishService.queryDishesByCategoryId(categoryId);
        return Result.success(dishes);
    }
}
