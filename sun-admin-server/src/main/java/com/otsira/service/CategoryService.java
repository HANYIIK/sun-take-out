package com.otsira.service;

import com.otsira.dto.CategoryInfoDTO;
import com.otsira.entity.Category;
import com.otsira.result.Page;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品分类管理的 service 层接口
 * @create: 2024/10/30 17:02
 */
public interface CategoryService {
    Page queryPage(Integer page, Integer pageSize, String name, String type);
    int insert(CategoryInfoDTO categoryInfoDTO);
    int delete(Long id);
    int update(CategoryInfoDTO categoryInfoDTO);
    int updateStatus(CategoryInfoDTO categoryInfoDTO);
    List<Category> queryListByType(Integer type);
}
