package com.otsira.aspect;

import com.otsira.annotation.AutoFill;
import com.otsira.constant.AutoFillConstant;
import com.otsira.enumeration.OperationType;
import com.otsira.util.EmployeeContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 自动填充 AOP 类
 * @create: 2024/10/30 20:10
 */
@Slf4j
@Aspect
@Component
public class AutoFillAspect {
    /**
     * 自动填充切点
     */
    @Pointcut("execution(* com.otsira.service.impl.*.*(..)) && @annotation(com.otsira.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 前置通知
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("自动填充 AOP 开始执行...");
        // 1. 获取被拦截方法上面 AutoFill 注解里的值（是 UPDATE 操作还是 INSERT 操作）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        /*
          2. 获取被拦截方法的参数
          规定：参数为 DTO 类型的前端返回数据 (EmployeeInfoDTO, CategoryInfoDTO, ...)
         */
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length < 1) {
            return;
        }
        Object entity = args[0];

        // 3. 获取赋值字段
        Long empId = EmployeeContext.getEmpId();
        LocalDateTime localTime = LocalDateTime.now();

        // 4. 根据不同的操作类型，进行赋值
        try {
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            if (operationType == OperationType.INSERT) {
                // INSERT 操作
                log.info("INSERT 操作，自动填充 createUser, createTime, updateUser 和 updateTime");
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                setCreateUser.invoke(entity, empId);
                setCreateTime.invoke(entity, localTime);
                setUpdateUser.invoke(entity, empId);
                setUpdateTime.invoke(entity, localTime);
            } else if (operationType == OperationType.UPDATE) {
                // UPDATE 操
                log.info("UPDATE 操作，自动填充 updateUser 和 updateTime");
                setUpdateUser.invoke(entity, empId);
                setUpdateTime.invoke(entity, localTime);
            } else {
                log.info("不支持的操作类型");
            }
        } catch (NoSuchMethodException e) {
            log.info("没有找到对应的方法");
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.info("方法不可访问");
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.info("调用目标异常");
            throw new RuntimeException(e);
        }
    }
}
