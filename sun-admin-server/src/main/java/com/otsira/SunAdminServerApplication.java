package com.otsira;

import com.otsira.config.FeignConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 管理员端的启动类
 * @create: 2024/10/19 18:23
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan("com.otsira.mapper")
@EnableCaching
@Slf4j
@EnableFeignClients(defaultConfiguration = FeignConfiguration.class) // 打开 openFeign 支持
public class SunAdminServerApplication {
    public static void main(String[] args) {
        log.info("admin server starting...");
        SpringApplication.run(SunAdminServerApplication.class, args);
        log.info("admin server started!");
    }
}
