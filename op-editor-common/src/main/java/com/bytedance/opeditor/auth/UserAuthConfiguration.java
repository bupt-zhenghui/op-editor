package com.bytedance.opeditor.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Configuration
public class UserAuthConfiguration {
    @Bean
    public AuthValidator authValidator() {
        return new AuthValidator();
    }

    @Bean
    public AuthLevelMethodInterceptor authLevelMethodInterceptor() {
        return new AuthLevelMethodInterceptor();
    }
}
