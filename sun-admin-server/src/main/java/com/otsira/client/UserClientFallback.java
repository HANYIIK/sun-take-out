package com.otsira.client;

import com.otsira.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 调用用户端微服务的熔断方法
 * @create: 2024/10/26 17:18
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
