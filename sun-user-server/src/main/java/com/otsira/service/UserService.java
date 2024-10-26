package com.otsira.service;

import com.otsira.entity.User;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户端的 service 层接口
 * @create: 2024/10/26 16:59
 */
public interface UserService {
    List<User> findAllUsers();
}
