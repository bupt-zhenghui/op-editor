package com.bytedance.opeditor.ref.mapper;

import com.bytedance.opeditor.ref.entity.RefEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Repository
public interface RefMapper extends MongoRepository<RefEntity, String> {
}
