package com.otsira.config;

import com.otsira.constant.JwtClaimsConstant;
import com.otsira.util.EmployeeContext;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 每次通过 openFeign 实现微服务之间的调用时，拦截请求，把当前登录的管理员 id 放到请求头里，供服务提供方使用。
 * @create: 2024/10/26 20:02
 */
@Slf4j
@Configuration
public class DefaultFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        log.info("openFeign 请求拦截器启动...");
        return requestTemplate -> {
            // 从请求上下文中获取当前登录的管理员 id
            Long empId = EmployeeContext.getEmpId();
            log.info("openFeign 请求拦截器获取到的管理员 id: {}", empId);
            if (empId != null) {
                // 把当前登录的管理员 id 放到请求头里
                requestTemplate.header(JwtClaimsConstant.EMP_ID, empId.toString());
            }
        };
    }
}
