package com.bytedance.opeditor.auth;

import com.bytedance.opeditor.annotation.WithAuthLevel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 权级校验方法级拦截器
 *
 * @author fordring
 * @since 2021/11/14
 */
@Aspect
@Component
@Slf4j
public class AuthLevelMethodInterceptor {
    @Resource
    private AuthValidator authValidator;

    @Around(value = "@annotation(com.bytedance.opeditor.annotation.WithAuthLevel) ||" +
            "@within(com.bytedance.opeditor.annotation.WithAuthLevel)")
    public Object invoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("已进入权级校验器");
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        WithAuthLevel annotation;
        if (method.isAnnotationPresent(WithAuthLevel.class)) {
            log.debug("通过方法标注进入校验器");
            annotation = method.getAnnotation(WithAuthLevel.class);
        } else {
            log.debug("通过类标注进入校验器");
            annotation = proceedingJoinPoint.getTarget().getClass().getAnnotation(WithAuthLevel.class);
        }
        if(annotation==null) {
            log.error("权级校验器被闯入,请检查切点设置是否正常。");
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "服务器状态异常");
        }
        if(!authValidator.accessAuthLevel(annotation.value(), annotation.actual()))
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "权限不足");
        log.debug("校验通过");
        return proceedingJoinPoint.proceed();
    }
}
