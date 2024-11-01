package com.otsira.config;

import com.otsira.properties.AliyunOssProperties;
import com.otsira.util.AliyunOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 阿里云OSS文件上传配置类
 * @create: 2024/11/01 19:59
 */
@Configuration
@Slf4j
public class AliyunOssConfiguration {
    private AliyunOssProperties aliyunOssProperties;

    @Autowired
    public void setAliyunOssProperties(AliyunOssProperties aliyunOssProperties) {
        this.aliyunOssProperties = aliyunOssProperties;
    }

    @Bean
    public AliyunOssUtil createAliyunOssUtil() {
        log.info("创建阿里云OSS文件上传工具类对象...");
        return new AliyunOssUtil(this.aliyunOssProperties);
    }
}
