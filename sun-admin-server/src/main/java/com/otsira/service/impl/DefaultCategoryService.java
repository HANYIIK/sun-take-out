package com.otsira.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.annotation.AutoFill;
import com.otsira.constant.CategoryTypeConstant;
import com.otsira.constant.MessageConstant;
import com.otsira.constant.StatusConstant;
import com.otsira.dto.CategoryInfoDTO;
import com.otsira.dto.DishInfoDTO;
import com.otsira.dto.SetmealInfoDTO;
import com.otsira.entity.Category;
import com.otsira.entity.Dish;
import com.otsira.entity.Setmeal;
import com.otsira.enumeration.OperationType;
import com.otsira.exception.CategoryNameConflictException;
import com.otsira.exception.CategoryNotFoundException;
import com.otsira.exception.DeletionNotAllowedException;
import com.otsira.mapper.CategoryMapper;
import com.otsira.result.Page;
import com.otsira.service.CategoryService;
import com.otsira.service.DishService;
import com.otsira.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品分类管理的 service 层默认实现类
 * @create: 2024/10/30 17:03
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class DefaultCategoryService implements CategoryService {
    private CategoryMapper categoryMapper;
    private DishService dishService;
    private SetmealService setmealService;
    private ObjectMapper objectMapper;

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }

    @Autowired
    public void setSetmealService(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @AutoFill(OperationType.INSERT)
    public int insert(CategoryInfoDTO categoryInfoDTO) {
        // 首先，要校验菜品分类名称 name 是否重复
        if (categoryMapper.selectByName(categoryInfoDTO.getName()) != null) {
            // 重复，抛异常
            throw new CategoryNameConflictException(MessageConstant.CATEGORY_NAME_CONFLICT);
        }
        // 没有重复，继续添加
        // 设置默认启用状态为禁用
        categoryInfoDTO.setStatus(StatusConstant.DISABLE);
        Category category = objectMapper.convertValue(categoryInfoDTO, Category.class);
        return categoryMapper.insertSelective(category);
    }

    @Override
    public int delete(Long id) {
        // 1.删除菜品分类前，需要判断该分类下是否有菜品
        List<Dish> dishes = dishService.queryByCategoryId(id);
        if (dishes != null && !dishes.isEmpty()) {
            // 该分类下有菜品，不允许删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 2.删除菜品分类前，需要判断该分类下是否有套餐
        List<Setmeal> setmeals = setmealService.querySetmealByCategoryId(id);
        if (setmeals != null && !setmeals.isEmpty()) {
            // 该分类下有套餐，不允许删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 3.该菜品分类下没有菜品或套餐，可以删除
        return categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    @AutoFill(OperationType.UPDATE)
    public int update(CategoryInfoDTO categoryInfoDTO) {
        // 首先，要校验菜品分类名称 name 是否重复
        Category editingCategory = categoryMapper.selectByPrimaryKey(categoryInfoDTO.getId());
        String oldName = editingCategory.getName();
        if (!categoryInfoDTO.getName().equals(oldName)) {
            // 菜品名称被更改，需要校验与其他菜品名是否重复
            if (categoryMapper.selectByName(categoryInfoDTO.getName()) != null) {
                // 重复，抛异常
                throw new CategoryNameConflictException(MessageConstant.CATEGORY_NAME_CONFLICT);
            }
        }
        Category category = objectMapper.convertValue(categoryInfoDTO, Category.class);
        // 没有重复或菜品分类名称没有被更改，继续修改
        // Category category = Category.builder()
        //         .id(categoryInfoDTO.getId())
        //         .name(categoryInfoDTO.getName())
        //         .sort(categoryInfoDTO.getSort())
        //         .build();
        // category.setUpdateUser(empId);
        // category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    @AutoFill(OperationType.UPDATE)
    public int updateStatus(CategoryInfoDTO categoryInfoDTO) {
        if (categoryMapper.selectByPrimaryKey(categoryInfoDTO.getId()) == null) {
            throw new CategoryNotFoundException(MessageConstant.CATEGORY_NOT_FOUND);
        }
        Category category = objectMapper.convertValue(categoryInfoDTO, Category.class);

        // 更新数据库
        int updateDishStatus = 0;
        // 连带着把该分类下的子菜品或子套餐的状态也更新
        Integer type = categoryMapper.selectByPrimaryKey(categoryInfoDTO.getId()).getType();
        // 该分类是个菜品分类
        if (CategoryTypeConstant.DISH.equals(type)) {
            List<Dish> dishes = dishService.queryByCategoryId(categoryInfoDTO.getId());
            if (dishes != null && !dishes.isEmpty()) {
                for (Dish dish : dishes) {
                    DishInfoDTO dishInfoDTO = dishService.queryDishInfoDtoById(dish.getId());
                    dishInfoDTO.setStatus(categoryInfoDTO.getStatus());
                    updateDishStatus += dishService.update(dishInfoDTO);
                }
            }
        // 该分类是个套餐分类
        } else if (CategoryTypeConstant.SETMEAL.equals(type)) {
            List<Setmeal> setmeals = setmealService.querySetmealByCategoryId(categoryInfoDTO.getId());
            if (setmeals != null && !setmeals.isEmpty()) {
                for (Setmeal setmeal : setmeals) {
                    SetmealInfoDTO setmealInfoDTO = setmealService.querySetmealInfoDtoById(setmeal.getId());
                    setmealInfoDTO.setStatus(categoryInfoDTO.getStatus());
                    updateDishStatus += setmealService.update(setmealInfoDTO);
                }
            }
        }

        // 更新菜品分类状态
        return categoryMapper.updateByPrimaryKeySelective(category) + updateDishStatus;
    }

    @Override
    public List<Category> queryListByType(Integer type) {
        List<Category> categories = categoryMapper.queryByType(type);
        if (categories == null || categories.isEmpty()) {
            throw new CategoryNotFoundException(MessageConstant.CATEGORY_NOT_FOUND);
        }
        return categories;
    }

    @Override
    public Page queryPage(Integer page, Integer pageSize, String name, String type) {
        if (name == null) {
            name = "";
        }
        if (type == null) {
            type = "";
        }
        int total = categoryMapper.queryPageCount(name, type);
        Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer start = (page - 1) * pageSize;
        List<Category> records = categoryMapper.queryPage(start, pageSize, name, type);
        return Page.builder()
                .total(total)
                .records(records)
                .build();
    }

}
