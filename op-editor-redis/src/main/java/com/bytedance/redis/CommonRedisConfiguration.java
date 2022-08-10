package com.bytedance.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author fordring
 * @since 2021/11/18
 */
@Configuration
public class CommonRedisConfiguration {

    @Bean
    @Qualifier("springSessionDefaultRedisSerializer")
    public RedisSerializer<?> redisValueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(@Qualifier("springSessionDefaultRedisSerializer") RedisSerializer<?> redisValueSerializer){
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setValueSerializer(redisValueSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(redisValueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
