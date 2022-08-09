package com.bytedance.opeditor.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改密码表单
 *
 * @author onism
 * @since 2021/12/10
 */
@Schema(description = "修改密码表单")
@Data
public class PasswordForm {
    @NotBlank
    @Size(max = 60, min = 3, message = "密码的长度范围为{min}-{max}")
    @Schema(description = "原密码")
    String oldPassword;

    @NotBlank
    @Size(max = 60, min = 3, message = "密码的长度范围为{min}-{max}")
    @Schema(description = "新密码")
    String newPassword;

    @NotBlank
    @Size(max = 60, min = 3, message = "密码的长度范围为{min}-{max}")
    @Schema(description = "确认密码")
    String surePassword;
}
