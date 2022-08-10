package com.bytedance.opeditor.ref.service.version;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Service
@Slf4j
public class RedisVersionControl implements VersionControl{
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public long incrAndGetGlobalVersion() {
        return Optional.ofNullable(redisTemplate.opsForValue().increment("ref:globalVersion")).orElseThrow();
    }

    @Override
    public boolean compareAndSetVersion(String redId, long version) {
        String key = "ref:version:"+redId;
        int ttl = 10;
        while(ttl-->0) {
            try {
                Boolean res = redisTemplate.execute(new SessionCallback<>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public Boolean execute(@Nonnull RedisOperations operations) throws DataAccessException {
                        operations.watch(key);
                        operations.multi();
                        Object value = operations.opsForValue().get(key);
                        if (!ObjectUtils.isEmpty(value) && Long.parseLong(value.toString()) > version) {
                            return false;
                        }
                        log.info("指标{}版本更新至{}", key, version);
                        operations.opsForValue().set(key, version);
                        operations.exec();
                        return true;
                    }
                });
                if(res!=null) return res;
            } catch (Exception ignore){}
        }
        return false;
    }
}
