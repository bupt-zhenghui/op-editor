package com.bytedance.opeditor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fordring
 * @since 2022/8/9
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LiteralData {
    String value;
    String type;
}
