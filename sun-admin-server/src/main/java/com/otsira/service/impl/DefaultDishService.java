package com.otsira.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.annotation.AutoFill;
import com.otsira.constant.MessageConstant;
import com.otsira.constant.StatusConstant;
import com.otsira.dto.DishInfoDTO;
import com.otsira.dto.SetmealInfoDTO;
import com.otsira.entity.Dish;
import com.otsira.entity.DishFlavor;
import com.otsira.entity.SetmealDish;
import com.otsira.enumeration.OperationType;
import com.otsira.exception.DeletionNotAllowedException;
import com.otsira.exception.DishNameConflictException;
import com.otsira.exception.DishNotFoundException;
import com.otsira.mapper.DishMapper;
import com.otsira.result.Page;
import com.otsira.service.DishService;
import com.otsira.service.SetmealService;
import com.otsira.vo.DishWithFlavorsAndCategoryNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 菜品管理 service 层接口的默认实现类
 * @create: 2024/11/02 17:22
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class DefaultDishService implements DishService {
    private DishMapper dishMapper;
    private SetmealService setmealService;
    private ObjectMapper objectMapper;

    @Autowired
    public void setDishMapper(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
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
    public Page queryPage(Integer page, Integer pageSize, String name, Long categoryId, Integer status) {
        if (name == null) {
            name = "";
        }
        String categoryIdStr = categoryId == null ? "" : categoryId.toString();
        String statusStr = status == null ? "" : status.toString();
        int total = dishMapper.queryPageCount(name, categoryIdStr, statusStr);
        Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer start = (page - 1) * pageSize;
        List<DishWithFlavorsAndCategoryNameVO> records = dishMapper.queryPage(start, pageSize, name, categoryIdStr, statusStr);
        return Page.builder()
                .total(total)
                .records(records)
                .build();
    }

    @Override
    public DishInfoDTO queryDishInfoDtoById(Long id) {
        Dish dish = dishMapper.selectByPrimaryKey(id);
        if (dish == null) {
            throw new DishNotFoundException(MessageConstant.DISH_NOT_FOUND);
        }
        // 设置菜品口味List
        DishInfoDTO dishInfoDTO = objectMapper.convertValue(dish, DishInfoDTO.class);
        List<DishFlavor> flavors = dishMapper.queryFlavorByDishId(id);
        dishInfoDTO.setFlavors(flavors);

        return dishInfoDTO;
    }

    @Override
    @AutoFill(OperationType.UPDATE)
    @Transactional
    public int update(DishInfoDTO dishInfoDTO) {
        // 查询旧名称
        String oldName = dishMapper.selectByPrimaryKey(dishInfoDTO.getId()).getName();
        // 如果新名称和旧名称不一样, 则检查新名称是否重复
        if (!oldName.equals(dishInfoDTO.getName())) {
            Dish dishByName = dishMapper.queryByName(dishInfoDTO.getName());
            if (dishByName != null) {
                // 菜品名称已存在, 抛异常
                throw new DishNameConflictException(MessageConstant.DISH_NAME_CONFLICT);
            }
        }

        // 菜品名称没有被更改, 或新旧名称没有重复, 更新 Dish
        Dish dish = Dish.builder()
                .id(dishInfoDTO.getId())
                .name(dishInfoDTO.getName())
                .categoryId(dishInfoDTO.getCategoryId())
                .description(dishInfoDTO.getDescription())
                .image(dishInfoDTO.getImage())
                .price(dishInfoDTO.getPrice())
                .status(dishInfoDTO.getStatus())
                .createTime(dishInfoDTO.getCreateTime())
                .createUser(dishInfoDTO.getCreateUser())
                .updateTime(dishInfoDTO.getUpdateTime())
                .updateUser(dishInfoDTO.getUpdateUser())
                .build();
        int dishUpdate = dishMapper.updateByPrimaryKeySelective(dish);

        // 更新 Flavors
        dishMapper.deleteFlavorsByDishId(dish.getId());
        for (DishFlavor flavor : dishInfoDTO.getFlavors()) {
            flavor.setDishId(dish.getId());
            dishMapper.insertFlavor(flavor);
        }

        int updateSetmeal = 0;
        // 如果菜品售卖状态被更改为停售
        if (dishInfoDTO.getStatus().equals(StatusConstant.DISABLE)) {
            // 需要判断该菜品是否被套餐引用
            List<SetmealDish> setmealDishes = setmealService.querySetmealDishByDishId(dish.getId());
            if (setmealDishes != null && !setmealDishes.isEmpty()) {
                for (SetmealDish setmealDish : setmealDishes) {
                    SetmealInfoDTO setmealInfoDTO = setmealService.querySetmealInfoDtoById(setmealDish.getSetmealId());
                    // 如果被引用的套餐售卖状态为起售
                    if (setmealInfoDTO.getStatus().equals(StatusConstant.ENABLE)) {
                        // 则需要将套餐的售卖状态自动改为停售
                        setmealInfoDTO.setStatus(StatusConstant.DISABLE);
                        updateSetmeal += setmealService.update(setmealInfoDTO);
                    }
                }
            }
        }
        return dishUpdate + updateSetmeal;
    }

    @Override
    @AutoFill(OperationType.INSERT)
    @Transactional
    public int insert(DishInfoDTO dishInfoDTO) {
        // 菜品名称不能重复
        Dish dishByName = dishMapper.queryByName(dishInfoDTO.getName());
        if (dishByName != null) {
            // 菜品名称已存在, 抛异常
            throw new DishNameConflictException(MessageConstant.DISH_NAME_CONFLICT);
        }
        // 没有重复, 添加菜品
        Dish dish = Dish.builder()
                .id(dishInfoDTO.getId())
                .name(dishInfoDTO.getName())
                .categoryId(dishInfoDTO.getCategoryId())
                .description(dishInfoDTO.getDescription())
                .image(dishInfoDTO.getImage())
                .price(dishInfoDTO.getPrice())
                .status(dishInfoDTO.getStatus())
                .createTime(dishInfoDTO.getCreateTime())
                .createUser(dishInfoDTO.getCreateUser())
                .updateTime(dishInfoDTO.getUpdateTime())
                .updateUser(dishInfoDTO.getUpdateUser())
                .build();
        int insert = dishMapper.insert(dish);
        // 添加 flavors
        List<DishFlavor> flavors = dishInfoDTO.getFlavors();
        log.info("Generated id: {}", dish.getId());
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dish.getId());
            dishMapper.insertFlavor(flavor);
        }
        return insert;
    }

    @Override
    @Transactional
    public int deleteBatch(List<Long> ids) {
        int delete = 0;
        for (Long id : ids) {
            // 起售中的菜品不能删除
            if (dishMapper.selectByPrimaryKey(id).getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

            // 删除菜品前, 需要判断该菜品是否被套餐引用
            List<SetmealDish> setmealDishes = setmealService.querySetmealDishByDishId(id);
            if (setmealDishes != null && !setmealDishes.isEmpty()) {
                // 该菜品被套餐引用, 不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }

            // 删除菜品口味数据
            dishMapper.deleteFlavorsByDishId(id);
            // 删除菜品
            delete += dishMapper.deleteByPrimaryKey(id);
        }
        return delete;
    }

    @Override
    public List<Dish> queryByCategoryId(Long categoryId) {
        return dishMapper.queryByCategoryId(categoryId);
    }
}
