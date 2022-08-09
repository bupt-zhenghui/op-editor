package com.bytedance.rpc;

import com.bytedance.opeditor.utils.RequestUtils;
import feign.*;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.CollectionFormat;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static feign.Util.emptyToNull;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

/**
 * @author fordring
 * @since 2021/11/24
 */
@Configuration
@EnableDiscoveryClient
@ComponentScan
@EnableFeignClients("com.bytedance.opeditor.api")
@Slf4j
public class FeignConfiguration {
    @Bean
    public RequestInterceptor feignInterceptor() {
        return (template) -> {
            Target<?> target = template.feignTarget();
            String name = target.name();
            log.debug("接收到RPC[name:{},path:{},type:{}]", name, target.url(), target.type().getSimpleName());
            // Cookie透传
            String cookie = RequestUtils.request().getHeader("Cookie");
            if(!ObjectUtils.isEmpty(cookie)) {
                log.debug("透传cookies:{}", cookie);
                template.header("Cookie", cookie);
            }
        };
    }

    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new ResponseEntityDecoder(new SpringDecoder(messageConverters){
            @Override
            public Object decode(Response response, Type type) throws IOException, FeignException {
                Object decode = super.decode(response, type);
                Collection<String> setCookies = response.headers().get("Set-Cookie");
                if(!CollectionUtils.isEmpty(setCookies)) {
                    log.debug("透传来自RPC的Set-Cookie:{}", setCookies);
                    setCookies.forEach(c->RequestUtils.response().addHeader("Set-Cookie", c));
                }
                return decode;
            }
        });
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public Contract feignContract(
            ConversionService feignConversionService,
            @Autowired(required = false) List<AnnotatedParameterProcessor> parameterProcessors,
            @Autowired(required = false) FeignClientProperties feignClientProperties) {
        boolean decodeSlash = feignClientProperties == null || feignClientProperties.isDecodeSlash();
        if(parameterProcessors==null) parameterProcessors = new ArrayList<>();
        return new SpringMvcContract(parameterProcessors, feignConversionService, decodeSlash){
            @Override
            protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
                RequestMapping classAnnotation = findMergedAnnotation(clz, RequestMapping.class);
                if (classAnnotation != null) {
                    if (classAnnotation.value().length > 0) {
                        String pathValue = emptyToNull(classAnnotation.value()[0]);
                        if (pathValue != null) {
                            // Append path from @RequestMapping if value is present on method
                            if (!pathValue.startsWith("/") && !data.template().path().endsWith("/")) {
                                pathValue = "/" + pathValue;
                            }
                            data.template().uri(pathValue, true);
                            if (data.template().decodeSlash() != decodeSlash) {
                                data.template().decodeSlash(decodeSlash);
                            }
                        }
                    }
                }
                org.springframework.cloud.openfeign.CollectionFormat collectionFormat = findMergedAnnotation(clz, CollectionFormat.class);
                if (collectionFormat != null) {
                    data.template().collectionFormat(collectionFormat.value());
                }
            }
        };
    }
}
