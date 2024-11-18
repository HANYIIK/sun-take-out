package com.otsira.controller;

import com.otsira.constant.StatusConstant;
import com.otsira.dto.DishInertDTO;
import com.otsira.dto.DishInfoDTO;
import com.otsira.entity.Dish;
import com.otsira.result.Page;
import com.otsira.result.Result;
import com.otsira.service.DishService;
import com.otsira.util.EmployeeContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品管理的 controller 层
 * @create: 2024/11/02 17:14
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理相关接口")
@Slf4j
public class DishController {
    private DishService dishService;
    private RedisTemplate redisTemplate;

    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        log.info("管理员端菜品管理 Controller 层正在注入 RedisTemplate: {}", redisTemplate);
        this.redisTemplate = redisTemplate;
    }

    @ApiOperation("添加菜品")
    @PostMapping
    public Result<Object> insert(@RequestBody DishInertDTO dishInsertDTO) {
        DishInfoDTO dishInfoDTO = DishInfoDTO.builder()
                .id(dishInsertDTO.getId())
                .name(dishInsertDTO.getName())
                .categoryId(dishInsertDTO.getCategoryId())
                .price(dishInsertDTO.getPrice())
                .image(dishInsertDTO.getImage())
                .description(dishInsertDTO.getDescription())
                .status(dishInsertDTO.getStatus())
                .flavors(dishInsertDTO.getFlavors())
                .createTime(dishInsertDTO.getCreateTime())
                .updateTime(dishInsertDTO.getUpdateTime())
                .createUser(dishInsertDTO.getCreateUser())
                .updateUser(dishInsertDTO.getUpdateUser())
                .build();
        log.info("管理员 id-{} 正在添加菜品: {}", EmployeeContext.getEmpId(), dishInfoDTO);
        int add = dishService.insert(dishInfoDTO);
        if (add <= 0) {
            return Result.error("添加菜品失败");
        }
        // 清楚 Redis 缓存中的菜品信息
        String redisKey = "dish_" + dishInsertDTO.getCategoryId();
        redisTemplate.delete(redisKey);
        return Result.success();
    }

    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<Page> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             // 菜品名的模糊查询
                             @RequestParam(value = "name", required = false) String name,
                             // 菜品的分类
                             @RequestParam(value = "categoryId", required = false) Long categoryId,
                             // 菜品的状态
                             @RequestParam(value = "status", required = false) Integer status) {
        log.info("管理员 id-{} 正在请求获取所有菜品的分页信息", EmployeeContext.getEmpId());
        Page pageResult = dishService.queryPage(page, pageSize, name, categoryId, status);
        return Result.success(pageResult);
    }

    @ApiOperation("请求编辑菜品")
    @GetMapping("{id}")
    public Result<DishInfoDTO> edit(@PathVariable("id") Long id) {
        log.info("管理员 id-{} 正在请求编辑菜品 id-{}", EmployeeContext.getEmpId(), id);
        DishInfoDTO dishInfoDTO = dishService.queryDishInfoDtoById(id);
        return Result.success(dishInfoDTO);
    }

    @ApiOperation("提交编辑后的菜品信息")
    @PutMapping
    public Result<Object> update(@RequestBody DishInfoDTO dishInfoDTO) {
        log.info("管理员 id-{} 正在提交编辑后的菜品信息: {}", EmployeeContext.getEmpId(), dishInfoDTO);
        int update = dishService.update(dishInfoDTO);
        if (update <= 0) {
            return Result.error("更新菜品信息失败");
        }
        // 删除 Redis 缓存中的所有菜品信息
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @ApiOperation("更改菜品的售卖状态")
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        log.info("管理员 id-{} 正在更改菜品 id-{} 的售卖状态为: {}", EmployeeContext.getEmpId(), id, Objects.equals(status, StatusConstant.ENABLE) ? "启售" : "下架");
        DishInfoDTO dishInfoDTO = dishService.queryDishInfoDtoById(id);
        dishInfoDTO.setStatus(status);
        int update = dishService.update(dishInfoDTO);
        if (update <= 0) {
            return Result.error("更改菜品售卖状态失败");
        }
        // 删除 Redis 缓存中的菜品信息
        String redisKey = "dish_" + dishInfoDTO.getCategoryId();
        redisTemplate.delete(redisKey);
        return Result.success();
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result<Object> delete(@RequestParam("ids") @RequestBody List<Long> ids) {
        log.info("管理员 id-{} 正在删除菜品 ids-{}", EmployeeContext.getEmpId(), ids);
        int delete = dishService.deleteBatch(ids);
        if (delete <= 0) {
            return Result.error("删除菜品失败");
        }
        // 删除 Redis 缓存中的所有菜品信息
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @ApiOperation("根据菜品类别id查询所有当下类别中的所有菜品")
    @GetMapping("/list")
    public Result<List<Dish>> queryByCategoryId(@RequestParam("categoryId") Long categoryId) {
        log.info("管理员 id-{} 正在请求查询菜品类别 id-{} 下的所有菜品", EmployeeContext.getEmpId(), categoryId);
        List<Dish> dishList = dishService.queryByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
