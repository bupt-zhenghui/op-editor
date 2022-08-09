package com.bytedance.opeditor.user.domain;

import com.bytedance.opeditor.domain.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 缓存在会话中的用户缓存信息
 *
 * @author fordring
 * @since 2021/11/12
 */
@Data
public class SessionUserInfo implements Serializable {
    String id;
    String name;
    Integer authLevel;
    Date createTime;

    public SessionUserInfo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.authLevel = user.getAuthLevel();
        this.createTime = new Date();
    }
}
