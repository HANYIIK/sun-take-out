package com.otsira.filters;

import com.otsira.constant.JwtClaimsConstant;
import com.otsira.properties.AuthProperties;
import com.otsira.properties.JwtProperties;
import com.otsira.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: eureka-server
 * @author: HANYIIK
 * @description: jwt 登录校验全局过滤器
 * @create: 2024/10/24 18:33
 */
@Component
@Slf4j
public class AdminAuthGlobalFilter implements GlobalFilter, Ordered {
    private JwtProperties jwtProperties;
    private AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    public void setAuthProperties(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取 request
        ServerHttpRequest request = exchange.getRequest();

        // 2. 判断是否需要做登录拦截
        if (isExclude(request.getPath().toString()) || request.getPath().toString().contains("swagger")) {
            log.info("当前请求路径不需要登录拦截, 放行...");
            return chain.filter(exchange);
        }

        // 3. 获取 token
        log.info("当前请求路径需要登录拦截, 进行登录校验...");
        List<String> token = request.getHeaders().get("token");

        // 4. 校验并解析 token
        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = JwtUtil.parseToken(jwtProperties.getAdminSecretKey(), token.get(0));
                // 5. 传递用户信息
                Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
                log.info("当前登录的员工 id: {}", empId);
                ServerWebExchange webExchange = exchange.mutate()
                        .request(builder -> builder.header(JwtClaimsConstant.EMP_ID, empId.toString()))
                        .build();
                // 6. 放行
                return chain.filter(webExchange);
            } catch (Exception e) {
                log.info("token 解析失败: {}", e.getMessage());
            }
        }
        // 5. 拦截
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean isExclude(String path) {
        for (String excludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
