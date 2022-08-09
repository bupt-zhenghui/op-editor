package com.bytedance.opeditor.user.utils;

import com.bytedance.opeditor.domain.User;
import com.bytedance.opeditor.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author fordring
 * @since 2021/11/6
 */
@Slf4j
public class SessionUtils {
    public static final String SESSION_USER_UID_NAMESPACE = "uid";

    public static String sessionId() {
        return RequestUtils.session().getId();
    }

    public static void setUser(User user) {
        RequestUtils.session().setAttribute(SESSION_USER_UID_NAMESPACE, user.getId());
    }

    public static void destroySession() {
        RequestUtils.session().invalidate();
    }

    public static String uid() {
        if(RequestUtils.servletRequestAttributes()==null) throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "未开启会话");
        Object attribute = RequestUtils.session().getAttribute(SESSION_USER_UID_NAMESPACE);
        if(attribute==null) throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "尚未登录");
        return attribute.toString();
    }

    public static boolean hasLogin() {
        if(RequestUtils.servletRequestAttributes()==null) return false;
        Object attribute = RequestUtils.session().getAttribute(SESSION_USER_UID_NAMESPACE);
        return attribute != null;
    }
}
