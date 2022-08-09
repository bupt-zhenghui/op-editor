package com.bytedance.rpc;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Component
public class FeignWebMvcSupport implements WebMvcRegistrations {
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new FeignRequestMappingHandlerMapping();
    }

    public static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
                    (AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class)
                            && !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class)
                    ));
        }
    }
}
