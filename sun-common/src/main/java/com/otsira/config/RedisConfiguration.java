package com.otsira.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: spring data redis 的配置类
 * @create: 2024/11/09 17:52
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建 RedisTemplate ...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置 key 的序列化器, 默认为 jdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
