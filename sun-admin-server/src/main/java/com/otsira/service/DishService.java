package com.otsira.service;

import com.otsira.dto.DishInfoDTO;
import com.otsira.entity.Dish;
import com.otsira.result.Page;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品管理的 service 层接口
 * @create: 2024/11/02 17:21
 */
public interface DishService {
    /**
     * 分页查询菜品信息
     *
     * @param page      当前页
     * @param pageSize  每页显示的数据条数
     * @param name      菜品名
     * @param categoryId 菜品分类
     * @param status    菜品状态
     * @return 菜品信息的分页数据
     */
    Page queryPage(Integer page, Integer pageSize, String name, Long categoryId, Integer status);

    /**
     * 根据 id 查询菜品信息 DTO
     * @param id 菜品 id
     * @return 菜品信息 DTO
     */
    DishInfoDTO queryDishInfoDtoById(Long id);

    /**
     * 更新菜品信息
     * @param dishInfoDTO 菜品信息
     * @return 更新结果
     */
    int update(DishInfoDTO dishInfoDTO);

    /**
     * 添加菜品
     * @param dishInfoDTO 菜品信息
     * @return 添加结果
     */
    int insert(DishInfoDTO dishInfoDTO);

    int deleteBatch(List<Long> ids);

    /**
     * 根据分类 id 查询菜品
     * @param categoryId 分类 id
     * @return 菜品列表
     */
    List<Dish> queryByCategoryId(Long categoryId);
}
