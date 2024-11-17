package com.otsira.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otsira.constant.MessageConstant;
import com.otsira.dto.UserLoginDTO;
import com.otsira.entity.*;
import com.otsira.exception.LoginFailedException;
import com.otsira.mapper.UserMapper;
import com.otsira.properties.WechatProperties;
import com.otsira.service.UserService;
import com.otsira.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户 service 层接口的默认实现类
 * @create: 2024/10/26 16:59
 */
@Service
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class DefaultUserService implements UserService {
    private static final String WECHAT_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private UserMapper userMapper;
    private WechatProperties wechatProperties;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setWechatProperties(WechatProperties wechatProperties) {
        this.wechatProperties = wechatProperties;
    }

    @Override
    public List<User> findAllUsers() {
        return this.userMapper.selectAll();
    }

    /**
     * 微信登录
     * @param userLoginDTO 包含微信登录授权码的前端传过来的数据封装类
     * @return 用户信息
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 给微信官方发送 http client 请求, 获得用户的 openid
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", this.wechatProperties.getAppid());
        paramMap.put("secret", this.wechatProperties.getSecret());
        paramMap.put("js_code", userLoginDTO.getCode());
        paramMap.put("grant_type", "authorization_code");
        String response = HttpClientUtil.doGet(WECHAT_URL, paramMap);
        // 解析 openid
        JSONObject jsonObject = JSON.parseObject(response);
        String openid = jsonObject.getString("openid");

        // openid 为空, 说明没有获取到用户信息
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 查询数据库中是否有该用户
        User user = this.userMapper.queryUserByOpenid(openid);

        // 如果没有该用户, 则新建用户
        if (user == null) {
            log.info("用户 openid: {} 不存在, 新建用户", openid);
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            int insert = this.userMapper.insert(user);
            if (insert <= 0) {
                throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
            }
        }

        log.info("用户 openid: {} 登录成功", openid);
        return user;
    }
}
