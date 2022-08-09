package com.bytedance.opeditor.api.dto.ref;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Schema(description = "设置ref")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetRefForm {
    @NotBlank
    String id;
    String value;
}
