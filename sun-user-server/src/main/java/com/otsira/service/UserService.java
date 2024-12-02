package com.otsira.service;

import com.otsira.dto.UserLoginDTO;
import com.otsira.entity.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户端的 service 层接口
 * @create: 2024/10/26 16:59
 */
public interface UserService {
    List<User> findAllUsers();
    User wxLogin(UserLoginDTO userLoginDTO);

    /**
     * openFeign 调用方法
     * @param begin 开始时间
     * @param end 结束时间
     * @return 新增用户数和总用户数
     */
    List<Integer> findNewAndTotalUserNum(LocalDateTime begin, LocalDateTime end);
}
