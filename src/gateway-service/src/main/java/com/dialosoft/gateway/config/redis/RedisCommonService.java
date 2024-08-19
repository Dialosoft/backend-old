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
        try {
            // Attempt a simple operation to check if Redis is available
            return Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().ping() != null;
        } catch (Exception e) {
            // Log the error if Redis is not available
            log.error("Redis is not available: {}", e.getMessage());
            return false;
        }
    }
}
