package com.bytedance.opeditor.user.mapper;

import com.bytedance.opeditor.user.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author fordring
 * @since 2021/11/6
 */
@Repository
public interface UserMapper extends MongoRepository<UserEntity, String> {
    boolean existsByName(String name);
    UserEntity findByName(String name);
}
