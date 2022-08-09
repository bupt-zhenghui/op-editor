package com.bytedance.opeditor.user.service;

import com.bytedance.opeditor.api.dto.user.AuthForm;
import com.bytedance.opeditor.constant.AuthLevels;
import com.bytedance.opeditor.domain.User;
import com.bytedance.opeditor.user.entity.UserEntity;
import com.bytedance.opeditor.user.mapper.UserMapper;
import com.bytedance.opeditor.user.utils.SessionUtils;
import com.bytedance.opeditor.utils.BeanMapper;
import com.bytedance.opeditor.utils.WebContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;

/**
 * @author fordring
 * @since 2021/11/6
 */
@Slf4j
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public User login(AuthForm authForm) {
        logout();
        UserEntity userEntity = getByName(authForm.getName());
        if(userEntity==null||
                !passwordEncoder.matches(authForm.getPassword(), userEntity.getPassword())
        ) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "用户名或密码错误");
        User user = BeanMapper.map(userEntity, User::new);
        SessionUtils.setUser(user);
        log.debug("用户{}已成功登录账户{}", WebContext.remoteHost(), authForm.getName());
        return user;
    }

    public User info(String id) {
        if(id==null) return null;
        log.info("info:{}", id);
        return getUser(id);
    }

    public User register(AuthForm authForm) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(authForm.getName());
        userEntity.setPassword(authForm.getPassword());
        userEntity.setTextName(authForm.getName());
        userEntity.setAuthLevel(AuthLevels.NORMAL.val);
        userEntity = insertUser(userEntity);
        if(userEntity==null) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "用户名被占用");
        return login(authForm);
    }

    public void logout() {
        SessionUtils.destroySession();
    }

    public User getUser(String id) {
        return BeanMapper.map(get(id), User::new);
    }

    public boolean existName(String name) {
        return userMapper.existsByName(name);
    }

    public UserEntity insertUser(UserEntity userEntity) {
        if(existName(userEntity.getName())) return null;
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userMapper.save(userEntity);
    }
    public boolean accessAuthLevel(Integer level, boolean actual) {
        if(level == null) level = AuthLevels.NORMAL.val;
        if(level == AuthLevels.ANY.val) return true;
        if(level == AuthLevels.MAX.val) return false;
        User user = info(SessionUtils.uid());
        if(user==null) return false;
        int userLevel = user.getAuthLevel();
        if(actual) return level == userLevel;   // 对于actual模式，权级必须相同
        else return level <= userLevel;         // 对于默认模式，用户权级应大于目标权级
    }

    public boolean accessAuthLevel(AuthLevels level, boolean actual) {
        return accessAuthLevel(level.val, actual);
    }
    public boolean accessAuthLevel(AuthLevels level) {
        return accessAuthLevel(level, false);
    }

    private UserEntity get(String id) {
        return userMapper.findById(id).orElse(null);
    }

    private UserEntity getByName(String name) {
        return userMapper.findByName(name);
    }
}
