package com.otsira.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsira.annotation.AutoFill;
import com.otsira.constant.MessageConstant;
import com.otsira.constant.StatusConstant;
import com.otsira.dto.SetmealInfoDTO;
import com.otsira.entity.Setmeal;
import com.otsira.entity.SetmealDish;
import com.otsira.enumeration.OperationType;
import com.otsira.exception.DeletionNotAllowedException;
import com.otsira.exception.EnableStatusNotAllowedException;
import com.otsira.mapper.DishMapper;
import com.otsira.mapper.SetmealMapper;
import com.otsira.result.Page;
import com.otsira.service.SetmealService;
import com.otsira.vo.SetmealInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 套餐管理的 service 接口默认实现类
 * @create: 2024/11/04 20:02
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class DefaultSetmealService implements SetmealService {
    private SetmealMapper setmealMapper;
    private DishMapper dishMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public void setSetmealMapper(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    @Autowired
    public void setDishMapper(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
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
        int total = setmealMapper.queryPageCount(name, categoryIdStr, statusStr);
        Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer start = (page - 1) * pageSize;
        List<SetmealInfoVO> records = setmealMapper.queryPage(start, pageSize, name, categoryIdStr, statusStr);
        return Page.builder()
                .total(total)
                .records(records)
                .build();
    }

    @Override
    @AutoFill(OperationType.INSERT)
    @Transactional
    public int insert(SetmealInfoDTO setmealInfoDTO) {
        // 添加到套餐表 setmeal
        Setmeal setmeal = Setmeal.builder()
                .id(setmealInfoDTO.getId())
                .name(setmealInfoDTO.getName())
                .price(setmealInfoDTO.getPrice())
                .description(setmealInfoDTO.getDescription())
                .categoryId(setmealInfoDTO.getCategoryId())
                .image(setmealInfoDTO.getImage())
                .image(setmealInfoDTO.getImage())
                .status(setmealInfoDTO.getStatus())
                .createTime(setmealInfoDTO.getCreateTime())
                .createUser(setmealInfoDTO.getCreateUser())
                .updateTime(setmealInfoDTO.getUpdateTime())
                .updateUser(setmealInfoDTO.getUpdateUser())
                .build();
        int insert = setmealMapper.insert(setmeal);

        // 添加到套餐-菜品关系表 setmeal_dish
        for (SetmealDish setmealDish : setmealInfoDTO.getSetmealDishes()) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealMapper.insertSetmealDish(setmealDish);
        }

        return insert;
    }

    @Override
    public SetmealInfoDTO querySetmealInfoDtoById(Long id) {
        // 从数据库 setmeal 中查询简略套餐信息
        Setmeal setmeal = setmealMapper.selectByPrimaryKey(id);
        SetmealInfoDTO setmealInfoDTO = objectMapper.convertValue(setmeal, SetmealInfoDTO.class);

        // 从数据库 setmeal_dish 中查询套餐-菜品关系信息
        List<SetmealDish> setmealDishes = setmealMapper.querySetmealDishesBySetmealId(id);
        setmealInfoDTO.setSetmealDishes(setmealDishes);

        return setmealInfoDTO;
    }

    @Override
    @AutoFill(OperationType.UPDATE)
    @Transactional
    public int update(SetmealInfoDTO setmealInfoDTO) {
        List<SetmealDish> setmealDishes = setmealInfoDTO.getSetmealDishes();
        // 套餐内如果包含未起售的菜品，不允许修改套餐状态为启用
        if (setmealInfoDTO.getStatus().equals(StatusConstant.ENABLE)) {
            for (SetmealDish setmealDish : setmealDishes) {
                if (dishMapper.selectByPrimaryKey(setmealDish.getDishId()).getStatus().equals(StatusConstant.DISABLE)) {
                    throw new EnableStatusNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }

        // 更新套餐表 setmeal
        Setmeal setmeal = Setmeal.builder()
                .id(setmealInfoDTO.getId())
                .name(setmealInfoDTO.getName())
                .price(setmealInfoDTO.getPrice())
                .description(setmealInfoDTO.getDescription())
                .categoryId(setmealInfoDTO.getCategoryId())
                .image(setmealInfoDTO.getImage())
                .image(setmealInfoDTO.getImage())
                .status(setmealInfoDTO.getStatus())
                .createTime(setmealInfoDTO.getCreateTime())
                .createUser(setmealInfoDTO.getCreateUser())
                .updateTime(setmealInfoDTO.getUpdateTime())
                .updateUser(setmealInfoDTO.getUpdateUser())
                .build();
        int update = setmealMapper.updateByPrimaryKeySelective(setmeal);

        // 更新套餐-菜品关系表 setmeal_dish
        setmealMapper.deleteSetmealDishesBySetmealId(setmeal.getId());
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealMapper.insertSetmealDish(setmealDish);
        }

        return update;
    }

    @Override
    @Transactional
    public int deleteBatch(List<Long> ids) {
        int delete = 0;
        for (Long id : ids) {
            // 起售中的套餐不能删除 SETMEAL_ON_SALE
            Setmeal setmeal = setmealMapper.selectByPrimaryKey(id);
            if (setmeal.getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
            delete += setmealMapper.deleteByPrimaryKey(id);
            setmealMapper.deleteSetmealDishesBySetmealId(id);
        }
        return delete;
    }

    @Override
    public List<SetmealDish> querySetmealDishByDishId(Long id) {
        return setmealMapper.querySetmealDishesByDishId(id);
    }

    @Override
    public List<Setmeal> querySetmealByCategoryId(Long id) {
        return setmealMapper.querySetmealByCategoryId(id);
    }

}
