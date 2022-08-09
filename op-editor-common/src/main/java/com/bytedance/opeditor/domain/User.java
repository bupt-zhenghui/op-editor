package com.bytedance.opeditor.domain;

import lombok.Data;

import java.util.Date;

/**
 * 保存用户信息
 *
 * @author fordring
 * @since 2021/11/5
 */
@Data
public class User {
    String id;
    String name;
    Integer authLevel;
    Date createTime;
}
