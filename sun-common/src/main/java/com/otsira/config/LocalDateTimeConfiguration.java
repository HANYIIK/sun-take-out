package com.otsira.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: LocalDateTime 与 Json 的自定义转换器
 * @create: 2024/11/01 21:50
 */
@Configuration
@Slf4j
public class LocalDateTimeConfiguration {

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        log.info("开始配置 LocalDateTime 与 Json 的自定义转换器...");
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, localDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}