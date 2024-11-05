package com.otsira.service;

import com.otsira.dto.SetmealInfoDTO;
import com.otsira.result.Page;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 套餐管理的 service 层接口
 * @create: 2024/11/04 20:01
 */
public interface SetmealService {
    /**
     * 分页查询套餐
     * @param page 页码
     * @param pageSize 每页显示数量
     * @param name 套餐名称
     * @param categoryId 套餐分类
     * @param status 套餐状态
     * @return Page 分页结果<SetmealInfoVO>
     */
    Page queryPage(Integer page, Integer pageSize, String name, Long categoryId, Integer status);

    /**
     * 添加套餐
     * @param setmealInfoDTO 套餐信息
     * @return int 影响行数
     */
    int insert(SetmealInfoDTO setmealInfoDTO);

    /**
     * 查询套餐详情信息
     * @param id 套餐id
     * @return SetmealInfoDTO 套餐详情信息
     */
    SetmealInfoDTO querySetmealInfoDtoById(Long id);

    /**
     * 更新套餐信息
     * @param setmealInfoDTO 套餐详情信息
     * @return int 影响行数
     */
    int update(SetmealInfoDTO setmealInfoDTO);

    /**
     * 批量删除套餐
     * @param ids 套餐id列表
     * @return int 影响行数
     */
    int deleteBatch(List<Long> ids);
}
