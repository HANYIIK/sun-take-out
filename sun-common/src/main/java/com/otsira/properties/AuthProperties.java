package com.otsira.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 需要进行登录校验的路径配置
 * @create: 2024/10/25 14:47
 */
@Component
@ConfigurationProperties(prefix = "auth.sun.path")
@Data
public class AuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
