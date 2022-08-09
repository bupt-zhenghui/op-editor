package com.bytedance.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;

/**
 * @author fordring
 * @since 2021/11/12
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "app.session.type", havingValue = "com/bytedance/redis")
@EnableRedisHttpSession(redisNamespace = "session")
public class RedisSessionConfiguration {
    @PostConstruct
    public void init() {
        log.info("session类型: Redis");
    }

}
