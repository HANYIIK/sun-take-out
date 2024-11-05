package com.otsira.controller;

import com.otsira.dto.SetmealInfoDTO;
import com.otsira.dto.SetmealInsertDTO;
import com.otsira.result.Page;
import com.otsira.result.Result;
import com.otsira.service.SetmealService;
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
 * @description: 套餐管理的 controller 层
 * @create: 2024/11/04 19:58
 */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐管理相关接口")
public class SetmealController {
    private SetmealService setmealService;

    @Autowired
    public void setSetmealService(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @ApiOperation("查询套餐分页列表")
    @GetMapping("/page")
    public Result<Page> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             // 套餐名称模糊查询
                             @RequestParam(value = "name", required = false) String name,
                             // 套餐分类
                             @RequestParam(value = "categoryId", required = false) Long categoryId,
                             // 套餐状态
                             @RequestParam(value = "status", required = false) Integer status) {
        log.info("管理员 id-{} 正在查询套餐列表", EmployeeContext.getEmpId());
        Page pageResult = setmealService.queryPage(page, pageSize, name, categoryId, status);
        return Result.success(pageResult);
    }

    @ApiOperation("添加套餐")
    @PostMapping
    public Result<SetmealInfoDTO> insert(@RequestBody SetmealInsertDTO setmealInsertDTO) {
        SetmealInfoDTO setmealInfoDTO = SetmealInfoDTO.builder()
                .id(setmealInsertDTO.getId())
                .categoryId(setmealInsertDTO.getCategoryId())
                .name(setmealInsertDTO.getName())
                .price(setmealInsertDTO.getPrice())
                .status(setmealInsertDTO.getStatus())
                .description(setmealInsertDTO.getDescription())
                .image(setmealInsertDTO.getImage())
                .setmealDishes(setmealInsertDTO.getSetmealDishes())
                .createTime(setmealInsertDTO.getCreateTime())
                .createUser(setmealInsertDTO.getCreateUser())
                .updateTime(setmealInsertDTO.getUpdateTime())
                .updateUser(setmealInsertDTO.getUpdateUser())
                .build();
        log.info("管理员 id-{} 正在添加套餐: {}", EmployeeContext.getEmpId(), setmealInfoDTO);
        int insert = setmealService.insert(setmealInfoDTO);
        if (insert <= 0) {
            return Result.error("添加套餐失败");
        }
        return Result.success();
    }

    @ApiOperation("请求编辑套餐的详细信息")
    @GetMapping("{id}")
    public Result<SetmealInfoDTO> edit(@PathVariable Long id) {
        log.info("管理员 id-{} 正在请求编辑套餐 id-{} 的详细信息", EmployeeContext.getEmpId(), id);
        SetmealInfoDTO setmealInfoDTO = setmealService.querySetmealInfoDtoById(id);
        return Result.success(setmealInfoDTO);
    }

    @ApiOperation("更新套餐")
    @PutMapping
    public Result<Object> update(@RequestBody SetmealInsertDTO setmealInsertDTO) {
        SetmealInfoDTO setmealInfoDTO = SetmealInfoDTO.builder()
                .id(setmealInsertDTO.getId())
                .categoryId(setmealInsertDTO.getCategoryId())
                .name(setmealInsertDTO.getName())
                .price(setmealInsertDTO.getPrice())
                .status(setmealInsertDTO.getStatus())
                .description(setmealInsertDTO.getDescription())
                .image(setmealInsertDTO.getImage())
                .setmealDishes(setmealInsertDTO.getSetmealDishes())
                .createTime(setmealInsertDTO.getCreateTime())
                .createUser(setmealInsertDTO.getCreateUser())
                .updateTime(setmealInsertDTO.getUpdateTime())
                .updateUser(setmealInsertDTO.getUpdateUser())
                .build();
        log.info("管理员 id-{} 正在更新套餐: {}", EmployeeContext.getEmpId(), setmealInfoDTO);
        int update = setmealService.update(setmealInfoDTO);
        if (update <= 0) {
            return Result.error("更新套餐失败");
        }
        return Result.success();
    }

    @ApiOperation("修改套餐的售卖状态")
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        log.info("管理员 id-{} 正在修改套餐 id-{} 的售卖状态为: {}", EmployeeContext.getEmpId(), id, status);
        SetmealInfoDTO setmealInfoDTO = setmealService.querySetmealInfoDtoById(id);
        setmealInfoDTO.setStatus(status);
        int update = setmealService.update(setmealInfoDTO);
        if (update <= 0) {
            return Result.error("修改套餐售卖状态失败");
        }
        return Result.success();
    }

    @ApiOperation("删除套餐")
    @DeleteMapping
    public Result<Object> delete(@RequestParam("ids") @RequestBody List<Long> ids) {
        log.info("管理员 id-{} 正在删除套餐: {}", EmployeeContext.getEmpId(), ids);
        int delete = setmealService.deleteBatch(ids);
        if (delete <= 0) {
            return Result.error("删除套餐失败");
        }
        return Result.success();
    }
}
