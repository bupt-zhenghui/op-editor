package com.bytedance.opeditor.annotation;

import com.bytedance.opeditor.constant.AuthLevels;

import java.lang.annotation.*;

/**
 * 基于AOP的方法级权限控制注解
 *
 * @author fordring
 * @since 2021/11/11
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WithAuthLevel {
    /**
     * 调用某方法的最低权级要求
     */
    AuthLevels value() default AuthLevels.NORMAL;

    /**
     * 是否严格匹配（即用户权级必须等于目标权级）
     */
    boolean actual() default false;
}
