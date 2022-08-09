package com.bytedance.opeditor.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fordring
 * @since 2021/11/12
 */
public class WebContext {
    private static ServletRequestAttributes servletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    public static HttpServletRequest request() {
        return servletRequestAttributes().getRequest();
    }
    public static HttpServletResponse response() {
        return servletRequestAttributes().getResponse();
    }

    /**
     * @return 本次请求的客户端ip
     */
    public static String remoteHost() {
        return request().getRemoteHost();
    }

    /**
     * @return 本次请求的method
     */
    public static String method() {
        return request().getMethod();
    }

    /**
     * @return 本次请求的路径
     */
    public static String path() {
        return request().getServletPath();
    }

    /**
     * 一个格式化过的字符串用来描述一个请求的信息
     *
     * @return 例如: 192.168.10.1->GET /user/login
     */
    public static String fmtReq() {
        return remoteHost()+"->"+method()+" "+path();
    }
}
