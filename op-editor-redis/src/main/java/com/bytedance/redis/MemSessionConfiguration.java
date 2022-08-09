package com.bytedance.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fordring
 * @since 2021/11/12
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "app.session.type", havingValue = "mem", matchIfMissing = true)
@EnableSpringHttpSession
public class MemSessionConfiguration {
    @PostConstruct
    public void init() {
        log.info("session类型: mem");
    }

    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }
}
