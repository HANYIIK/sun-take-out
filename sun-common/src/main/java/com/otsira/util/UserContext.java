package com.otsira.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 存取登录用户信息 ThreadLocal 的上下文
 * @create: 2024/11/16 15:21
 */
@Slf4j
public class UserContext {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        // log.info("{}: 当前登录员工 ID: {} 已存入 ThreadLocal 中...", Thread.currentThread().getId(), userId);
        THREAD_LOCAL.set(userId);
    }

    public static Long getUserId() {
        // log.info("{}: 当前登录员工 ID: {} 已从 ThreadLocal 中取出...", Thread.currentThread().getId(), THREAD_LOCAL.get());
        return THREAD_LOCAL.get();
    }

    public static void removeUserId() {
        // log.info("{}: 当前登录员工 ID: {} 已从 ThreadLocal 中移除...", Thread.currentThread().getId(), THREAD_LOCAL.get());
        THREAD_LOCAL.remove();
    }
}
