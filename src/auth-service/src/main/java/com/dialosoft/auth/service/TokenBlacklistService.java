package com.dialosoft.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
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
}