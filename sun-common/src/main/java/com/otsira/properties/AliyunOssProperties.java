package com.otsira.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 阿里云OSS配置类
 * @create: 2024/11/01 18:50
 */
@Component
@Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssProperties {
    private String endpoint;
    // 已放入系统变量
    private String accessKeyId;
    // 已放入系统变量
    private String accessKeySecret;
    private String bucketName;
    private String region;
    private String fileDir;
    private String urlPrefix;
}
