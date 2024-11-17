package com.otsira.controller;

import com.otsira.entity.Category;
import com.otsira.result.Result;
import com.otsira.service.CategoryService;
import com.otsira.util.UserContext;
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
 * @description: 菜单类别的 controller 层
 * @create: 2024/11/16 15:14
 */
@RestController
@Slf4j
@RequestMapping("/user/category")
@Api(tags = "菜品类别相关接口")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation("查询所有菜品类别")
    @GetMapping("/list")
    public Result<List<Category>> list(@RequestParam(value = "type", required = false) String type) {
        log.info("用户 id-{} 请求查询所有菜品类型", UserContext.getUserId());
        List<Category> categories = categoryService.queryCategoriesByType(type);
        return Result.success(categories);
    }
}
