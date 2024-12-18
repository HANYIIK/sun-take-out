package com.otsira.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 存取登录员工信息 ThreadLocal 的上下文
 * @create: 2024/10/26 15:16
 */
@Slf4j
public class EmployeeContext {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setEmpId(Long empId) {
        // log.info("{}: 当前登录员工 ID: {} 已存入 ThreadLocal 中...", Thread.currentThread().getId(), empId);
        THREAD_LOCAL.set(empId);
    }

    public static Long getEmpId() {
        // log.info("{}: 当前登录员工 ID: {} 已从 ThreadLocal 中取出...", Thread.currentThread().getId(), THREAD_LOCAL.get());
        return THREAD_LOCAL.get();
    }

    public static void removeEmpId() {
        // log.info("{}: 当前登录员工 ID: {} 已从 ThreadLocal 中移除...", Thread.currentThread().getId(), THREAD_LOCAL.get());
        THREAD_LOCAL.remove();
    }
}
