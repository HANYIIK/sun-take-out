package com.otsira.filters;

import com.otsira.constant.JwtClaimsConstant;
import com.otsira.properties.AuthPathProperties;
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
@SuppressWarnings("LoggingSimilarMessage")
@Component
@Slf4j
public class AdminAuthGlobalFilter implements GlobalFilter, Ordered {
    private JwtProperties jwtProperties;
    private AuthPathProperties authPathProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    public void setAuthProperties(AuthPathProperties authPathProperties) {
        this.authPathProperties = authPathProperties;
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
        Claims claims;
        ServerWebExchange webExchange;
        List<String> token;

        // 3.1 来自管理端
        if (request.getPath().toString().contains("/admin")) {
            log.info("当前管理端请求路径需要登录拦截, 进行登录校验...");
            token = request.getHeaders().get(jwtProperties.getAdminTokenName());

            // 4. 校验并解析 token
            if (token != null && !token.isEmpty()) {
                try {
                    claims = JwtUtil.parseToken(jwtProperties.getAdminSecretKey(), token.get(0));
                    // 5. 传递管理员信息
                    Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
                    log.info("当前登录的员工 id: {}", empId);
                    webExchange = exchange.mutate()
                            .request(builder -> builder.header(JwtClaimsConstant.EMP_ID, empId.toString()))
                            .build();
                    // 6. 放行
                    return chain.filter(webExchange);
                } catch (Exception e) {
                    log.info("token 解析失败: {}", e.getMessage());
                }
            }
        // 3.2 来自用户端
        } else if (request.getPath().toString().contains("/user")) {
            log.info("当前用户端请求路径需要登录拦截, 进行登录校验...");
            token = request.getHeaders().get(jwtProperties.getUserTokenName());

            // 4. 校验并解析 token
            if (token != null && !token.isEmpty()) {
                try {
                    claims = JwtUtil.parseToken(jwtProperties.getUserSecretKey(), token.get(0));
                    // 5. 传递用户信息
                    Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
                    log.info("当前登录的用户 id: {}", userId);
                    webExchange = exchange.mutate()
                            .request(builder -> builder.header(JwtClaimsConstant.USER_ID, userId.toString()))
                            .build();
                    // 6. 放行
                    return chain.filter(webExchange);
                } catch (Exception e) {
                    log.info("token 解析失败: {}", e.getMessage());
                }
            }
        }

        // 解析异常, 或系统发生异常, 统一进行 401 拦截
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean isExclude(String path) {
        for (String excludePath : authPathProperties.getExcludePaths()) {
            log.info("excludePath: {}, now path: {}", excludePath, path);
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
