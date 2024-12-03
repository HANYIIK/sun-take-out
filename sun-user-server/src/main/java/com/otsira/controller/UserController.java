package com.otsira.controller;

import com.otsira.constant.JwtClaimsConstant;
import com.otsira.dto.UserLoginDTO;
import com.otsira.entity.User;
import com.otsira.properties.JwtProperties;
import com.otsira.result.Result;
import com.otsira.service.UserService;
import com.otsira.util.JwtUtil;
import com.otsira.vo.UserLoginVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户端的 controller 层
 * @create: 2024/10/26 16:59
 */
@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "用户端相关接口")
public class UserController {
    private UserService userService;
    private JwtProperties jwtProperties;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @GetMapping("/findAllUsers")
    public List<User> findAll() {
        log.info("管理端请求查询所有用户");
        return this.userService.findAllUsers();
    }

    /**
     * 用户登录
     * @param userLoginDTO 前端返回过来的数据封装类, 只包含一个 String 类型的微信授权码
     * @return 登录用户的 id, openid, jwt 令牌
     * @depription: 通过微信授权码获取用户的 openid, 并生成 jwt 令牌返回给前端; 如果没有用户信息, 则新建用户
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户请求登录, 微信授权码: {}", userLoginDTO.getCode());
        User user = userService.wxLogin(userLoginDTO);
        // 生成 token
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.generateToken(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * 查询新增用户和总用户数(openFeign调用)
     * @param begin 开始时间
     * @param end 结束时间
     * @return 新增用户数和总用户数
     */
    @GetMapping("/findNewAndTotalUserNum")
    public List<Integer> findNewAndTotal(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime begin,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        log.info("管理端请求查询新增用户和总用户数");
        return this.userService.findNewAndTotalUserNum(begin, end);
    }

}
