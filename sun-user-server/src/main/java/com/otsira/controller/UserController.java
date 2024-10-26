package com.otsira.controller;

import com.otsira.entity.User;
import com.otsira.service.UserService;
import com.otsira.util.EmployeeContext;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户端的 controller 层
 * @create: 2024/10/26 16:59
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户端相关接口")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findAllUsers")
    public List<User> findAll() {
        log.info("管理员 id {} 请求查询所有用户", EmployeeContext.getEmpId());
        return this.userService.findAllUsers();
    }
}
