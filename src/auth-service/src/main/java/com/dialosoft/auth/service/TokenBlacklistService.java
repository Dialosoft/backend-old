package com.dialosoft.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToBlacklist(String token, Long expirationTime) {
        if (!isTokenBlacklisted(token)) {
            // Use the token as a key and assign an expiration in seconds
            redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.SECONDS);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

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