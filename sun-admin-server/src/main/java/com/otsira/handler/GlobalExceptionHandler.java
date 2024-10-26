package com.otsira.handler;

import com.otsira.constant.MessageConstant;
import com.otsira.exception.BaseException;
import com.otsira.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 全局异常处理器
 * @create: 2024/10/21 17:44
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public Result<T> exceptionHandler(BaseException e) {
        log.error("用户操作异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<T> exceptionHandler(RuntimeException e) {
        log.error("系统发生异常：{}", e.getMessage());
        return Result.error(MessageConstant.SYSTEM_ERROR);
    }
}
