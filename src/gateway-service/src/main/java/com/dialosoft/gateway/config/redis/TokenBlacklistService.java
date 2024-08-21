package com.dialosoft.gateway.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenBlacklistService extends RedisCommonService {

    public TokenBlacklistService(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public void addToBlacklist(String token, Long expirationTime) {
        // Use the token as a key and assign an expiration in seconds
        redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return isRedisAvailable() && Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}