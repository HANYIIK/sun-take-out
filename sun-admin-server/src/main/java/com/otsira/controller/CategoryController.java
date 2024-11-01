package com.otsira.controller;

import com.otsira.constant.MessageConstant;
import com.otsira.constant.StatusConstant;
import com.otsira.dto.CategoryInfoDTO;
import com.otsira.entity.Category;
import com.otsira.result.Page;
import com.otsira.result.Result;
import com.otsira.service.CategoryService;
import com.otsira.util.EmployeeContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品分类管理的 controller 层
 * @create: 2024/10/30 17:00
 */
@RestController
@Slf4j
@RequestMapping("/admin/category")
@Api(tags = "菜品分类管理相关接口")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation("菜品分类分页查询")
    @GetMapping("/page")
    public Result<Page> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             // 菜品分类名的模糊查询
                             @RequestParam(value = "name", required = false) String name,
                             // 菜品分类的类型
                             @RequestParam(value = "type", required = false) String type) {
        log.info("管理员 {} 正在请求获取所有菜品分类信息, 当前页 {}, 一页 {} 条数据", EmployeeContext.getEmpId(), page, pageSize);
        Page pageResult = categoryService.queryPage(page, pageSize, name, type);
        return Result.success(pageResult);
    }

    @ApiOperation("新增菜品分类")
    @PostMapping
    public Result<Object> insert(@RequestBody CategoryInfoDTO categoryInfoDTO) {
        log.info("管理员 {} 正在请求新增菜品分类: {}", EmployeeContext.getEmpId(), categoryInfoDTO);
        int insert = categoryService.insert(categoryInfoDTO);
        if (insert == 1) {
            return Result.success();
        } else {
            return Result.error(MessageConstant.SYSTEM_ERROR);
        }
    }

    @ApiOperation("修改菜品分类")
    @PutMapping
    public Result<Object> update(@RequestBody CategoryInfoDTO categoryInfoDTO) {
        log.info("管理员 {} 正在请求修改菜品分类: {}", EmployeeContext.getEmpId(), categoryInfoDTO);
        int update = categoryService.update(categoryInfoDTO);
        if (update == 1) {
            return Result.success();
        } else {
            return Result.error(MessageConstant.SYSTEM_ERROR);
        }
    }

    @ApiOperation("删除菜品分类")
    @DeleteMapping
    public Result<Object> delete(@RequestParam("id") Long id) {
        log.info("管理员 {} 正在请求删除菜品分类: {}", EmployeeContext.getEmpId(), id);
        int delete = categoryService.delete(id);
        if (delete == 1) {
            return Result.success();
        } else {
            return Result.error(MessageConstant.SYSTEM_ERROR);
        }
    }

    @ApiOperation("菜品分类的禁用与启用")
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@PathVariable("status") Integer status,
                                       @RequestParam("id") Long id) {
        log.info("管理员 {} 正在请求{}菜品分类 id-{}", EmployeeContext.getEmpId(), status.equals(StatusConstant.ENABLE) ? "启用" : "禁用", id);
        CategoryInfoDTO categoryInfoDTO = CategoryInfoDTO.builder()
                .id(id)
                .status(status)
                .build();
        int updateStatus = categoryService.updateStatus(categoryInfoDTO);
        if (updateStatus == 1) {
            return Result.success();
        } else {
            return Result.error(MessageConstant.SYSTEM_ERROR);
        }
    }

    @ApiOperation("根据类型(1-菜品分类, 2-套餐分类)查询菜品分类")
    @GetMapping("/list")
    public Result<List<Category>> list(@RequestParam("type") Integer type) {
        log.info("管理员 {} 正在请求获取类型为 {} 的所有菜品分类", EmployeeContext.getEmpId(), type);
        return Result.success(categoryService.queryListByType(type));
    }
}
