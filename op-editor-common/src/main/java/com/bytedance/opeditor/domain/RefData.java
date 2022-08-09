package com.bytedance.opeditor.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author fordring
 * @since 2022/8/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RefData extends LiteralData {
    String id;

    Boolean isComputed;
    String formulaId;

    Date createTime;
    Date updateTime;
}
