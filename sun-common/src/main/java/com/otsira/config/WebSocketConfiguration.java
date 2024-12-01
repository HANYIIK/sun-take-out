package com.otsira.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: WebSocket 配置类
 * @create: 2024/11/26 18:54
 */
@Configuration
@Slf4j
@ConditionalOnClass(DispatcherServlet.class)
public class WebSocketConfiguration {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        log.info("正在加载 WebSocket 配置类...");
        return new ServerEndpointExporter();
    }
}
