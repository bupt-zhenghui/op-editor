package com.bytedance.opeditor.constant;

/**
 * 权限级
 *
 * @author fordring
 * @since 2021/11/9
 */
public enum AuthLevels {
    /**
     * 当一个服务需要ANY权限，意味着任何人都可以调用
     */
    ANY(0, "ANY"),
    BANNED(1, "被禁用"),
    NORMAL(5, "正常"),
    ADMIN(9, "管理员"),
    ROOT(13, "根"),
    /**
     * 当一个服务需要MAX权限，意味着没有任何人有权调用它
     */
    MAX(17, "MAX"),
    ;

    public final int val;
    public final String desc;

    AuthLevels(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
}
