package com.bytedance.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fordring
 * @since 2021/11/23
 */
@Configuration
@Import({CommonRedisConfiguration.class,MemSessionConfiguration.class, RedisSessionConfiguration.class})
public class RedisMidware {

}
