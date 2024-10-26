package com.otsira.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 生成 jwt 令牌相关配置
 * @create: 2024/10/20 20:19
 */
@Component
@ConfigurationProperties(prefix = "auth.sun.jwt")
@Data
public class JwtProperties {
    /**
     * 管理端员工生成 jwt 令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端微信用户生成 jwt 令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;
}
