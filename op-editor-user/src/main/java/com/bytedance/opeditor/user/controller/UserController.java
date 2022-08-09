package com.bytedance.opeditor.user.controller;

import com.bytedance.opeditor.api.UserApi;
import com.bytedance.opeditor.api.dto.user.AuthForm;
import com.bytedance.opeditor.domain.User;
import com.bytedance.opeditor.user.service.UserService;
import com.bytedance.opeditor.user.utils.SessionUtils;
import com.bytedance.opeditor.web.response.EmptyR;
import com.bytedance.opeditor.web.response.R;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author fordring
 * @since 2021/11/8
 */
@RestController
public class UserController implements UserApi {
    @Resource
    private UserService userService;

    @Override
    public R<User> login(@Valid AuthForm authForm) {
        return R.success(userService.login(authForm));
    }

    @Override
    public R<User> register(@Valid AuthForm authForm) {
        return R.success(userService.register(authForm));
    }

    @Override
    public R<String> uid() {
        return R.success(SessionUtils.uid());
    }

    @Override
    public R<String> sessionId() {
        return R.success(SessionUtils.sessionId());
    }

    @Override
    public R<User> info(String id) {
        if(id==null) id = SessionUtils.uid();
        return R.success(userService.info(id));
    }

    @Override
    public EmptyR logout() {
        userService.logout();
        return R.successEmpty();
    }

    @Override
    public R<Boolean> validAuthLevel(Integer level, boolean actual) {
        return R.success(userService.accessAuthLevel(level, actual));
    }
}
