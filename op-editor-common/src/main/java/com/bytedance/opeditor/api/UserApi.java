package com.bytedance.opeditor.api;

import com.bytedance.opeditor.api.dto.user.AuthForm;
import com.bytedance.opeditor.domain.User;
import com.bytedance.opeditor.web.response.EmptyR;
import com.bytedance.opeditor.web.response.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author fordring
 * @since 2021/11/5
 */
@ConditionalOnMissingClass("com.bytedance.opeditor.user.UserAutoConfiguration")
@FeignClient("${app.services.user:user}")
@Tag(name = "用户模块")
@RequestMapping("/user")
@ResponseBody
public interface UserApi {
    @Operation(summary = "用户登录") @PostMapping
    R<User> login(
            @RequestBody @Valid AuthForm authForm);

    @Operation(summary = "注册用户", description = "(并登录)") @PutMapping
    R<User> register(
            @RequestBody @Valid AuthForm authForm);

    @Operation(summary = "获取当前uid") @GetMapping("/uid")
    R<String> uid();

    @Operation(summary = "获取当前会话id") @GetMapping("/session")
    R<String> sessionId();

    @Operation(summary = "查看用户信息", description = "参数为空，则为当前用户") @GetMapping
    R<User> info(
            @RequestParam(value = "id", required = false) String id);

    @Operation(summary = "注销当前用户") @DeleteMapping
    EmptyR logout();

    @Operation(summary = "校验权级", description = "用户权级大于等于目标权级（对于actual模式，权级必须相同），则返回true") @GetMapping("/level/{level}")
    R<Boolean> validAuthLevel(
            @PathVariable("level") @Parameter(description = "目标权级") Integer level,
            @RequestParam(value = "actual", defaultValue = "false") boolean actual);
}
