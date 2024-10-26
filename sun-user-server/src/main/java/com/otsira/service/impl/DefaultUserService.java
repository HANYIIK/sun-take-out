package com.otsira.service.impl;

import com.otsira.entity.User;
import com.otsira.mapper.UserMapper;
import com.otsira.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户 service 层接口的默认实现类
 * @create: 2024/10/26 16:59
 */
@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class DefaultUserService implements UserService {
    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public List<User> findAllUsers() {
        return this.userMapper.selectAll();
    }
}
