package com.bytedance.opeditor.user.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * @author fordring
 * @since 2021/11/6
 */
@Document("user")
@Data
public class UserEntity {
    @Id
    String id;
    String name;
    String textName;
    String avatar;
    Date createTime;
    String password;
    Integer authLevel;
}
