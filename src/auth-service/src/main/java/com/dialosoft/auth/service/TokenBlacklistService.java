package com.dialosoft.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToBlacklist(String token, Long expirationTime) {
        if (isRedisAvailable() && !isTokenBlacklisted(token)) {
            // Use the token as a key and assign an expiration in seconds
            redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.SECONDS);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return isRedisAvailable() && Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

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