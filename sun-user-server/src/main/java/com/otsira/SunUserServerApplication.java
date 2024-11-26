package com.otsira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 用户的 springboot 项目启动类
 * @create: 2024/10/26 17:01
 */
@SpringBootApplication
@MapperScan("com.otsira.mapper")
@EnableCaching  // 开启缓存
@EnableScheduling   // 开启定时任务调度
public class SunUserServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunUserServerApplication.class, args);
    }
}
