package com.otsira.client;

import com.otsira.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 熔断方法
 * @create: 2024/10/27 14:30
 */
@Component
public class UserClientFallback implements UserClient {
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setName("findAllUsers 方法错误, 系统正忙...");
        users.add(user);
        return users;
    }
}
