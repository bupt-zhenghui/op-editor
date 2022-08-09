package com.bytedance.opeditor.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户认证表单
 *
 * @author fordring
 * @since 2021/11/5
 */
@Schema(description = "用户认证表单")
@Data @AllArgsConstructor @NoArgsConstructor
public class AuthForm {
    @NotBlank
    @Size(max = 60, min = 1, message = "用户名的长度范围为{min}-{max}")
    @Schema(description = "用户名")
    String name;

    @NotBlank
    @Size(max = 60, min = 3, message = "密码的长度范围为{min}-{max}")
    @Schema(description = "密码")
    String password;
}
