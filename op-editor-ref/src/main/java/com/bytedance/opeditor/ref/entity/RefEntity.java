package com.bytedance.opeditor.ref.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Data
@Document("ref")
public class RefEntity {
    @Id
    String id;
    String value;
    String type;

    Boolean isComputed;
    String formulaId;

    List<String> dependent;
    List<String> affect;

    Date createTime;
    Date updateTime;
}
