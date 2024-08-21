package com.dialosoft.gateway.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class RedisCommonService {

    protected final RedisTemplate<String, Object> redisTemplate;

    public boolean isRedisAvailable() {
        if (redisTemplate == null || redisTemplate.getConnectionFactory() == null) {
            return false;
        }
        try {
            return redisTemplate.getConnectionFactory().getConnection().ping() != null;
        } catch (Exception e) {
            log.error("Redis is not available: {}", e.getMessage());
            return false;
        }
    }
}
