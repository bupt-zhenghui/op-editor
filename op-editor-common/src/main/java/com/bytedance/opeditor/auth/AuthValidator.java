package com.bytedance.opeditor.auth;

import com.bytedance.opeditor.api.UserApi;
import com.bytedance.opeditor.constant.AuthLevels;
import com.bytedance.opeditor.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限校验器
 *
 * @author fordring
 * @since 2021/11/15
 */
@Component
@Slf4j
public class AuthValidator {
    @Resource @Lazy
    private UserApi userApi;
    /**
     * @return 获取当前用户信息
     */
    public User currentUser() {
        return userApi.info(null).getData();
    }

    /**
     * @return 获取当前用户的id
     */
    public String uid() {
        return userApi.uid().getData();
    }

    /**
     * 校验权级
     *
     * @param level 目标权级
     * @param actual 是否为actual模式
     * @return 当前用户能否获取到此权级
     */
    public boolean accessAuthLevel(AuthLevels level, boolean actual) {
        log.debug("试图获取全局权限<{},{}>",level, actual);
        if(AuthLevels.MAX.equals(level)) return false;
        if(AuthLevels.ANY.equals(level)) return true;
        return userApi.validAuthLevel(level.val, actual).getData();
    }
    public boolean accessAuthLevel(AuthLevels level) {
        return accessAuthLevel(level, false);
    }

    public String sessionId() {
        return userApi.sessionId().getData();
    }
}
