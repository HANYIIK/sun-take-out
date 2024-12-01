package com.otsira.controller;

import com.otsira.result.Result;
import com.otsira.service.WorkspaceService;
import com.otsira.vo.OrderOverViewVO;
import com.otsira.vo.OverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 工作台相关功能的 controller 层
 * @create: 2024/11/30 18:28
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台功能相关接口")
public class WorkspaceController {
    private WorkspaceService workspaceService;

    @Autowired
    public void setWorkspaceService(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @ApiOperation("获取菜品总览信息")
    @GetMapping("/overviewDishes")
    public Result<OverViewVO> overviewDishes() {
        return Result.success(workspaceService.overviewDishes());
    }

    @ApiOperation("获取套餐总览信息")
    @GetMapping("/overviewSetmeals")
    public Result<OverViewVO> overviewSetmeals() {
        return Result.success(workspaceService.overviewSetmeals());
    }

    @ApiOperation("获取订单总览信息")
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders() {
        return Result.success(workspaceService.overviewOrders());
    }

}
