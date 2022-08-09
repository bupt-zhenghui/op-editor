package com.bytedance.opeditor.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author fordring
 * @since 2021/11/24
 */
public class RequestUtils {
    public static ServletRequestAttributes servletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    public static HttpServletRequest request() {
        return servletRequestAttributes().getRequest();
    }
    public static HttpServletResponse response() {
        return servletRequestAttributes().getResponse();
    }
    public static HttpSession session() {
        return request().getSession();
    }
}
