package com.otsira.interceptor;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.otsira.constant.JwtClaimsConstant;
import com.otsira.util.UserContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用于获取登录用户信息的拦截器
 * @create: 2024/11/16 15:31
 */
@SuppressWarnings("NullableProblems")
@Component
@ConditionalOnClass(DispatcherServlet.class)
public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求头中的用户 ID 信息
        String userId = request.getHeader(JwtClaimsConstant.USER_ID);
        if (!StrUtil.isBlank(userId)) {
            // 存入 ThreadLocal 中
            UserContext.setUserId(Long.valueOf(userId));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.removeUserId();
    }
}
