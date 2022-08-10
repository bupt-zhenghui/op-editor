package com.bytedance.opeditor.ref;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author fordring
 * @since 2022/8/9
 */
@ComponentScan
@Configuration
@EnableMongoRepositories("com.bytedance.opeditor.ref.mapper")
public class RefAutoConfiguration {
}
