package com.dialosoft.gateway.config.redis;

import com.dialosoft.gateway.config.error.exception.CustomTemplateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisCacheService extends RedisCommonService {


    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public <T> void addInfoToCache(String token, String userId, T cacheInfo, Long expirationTime) {
        if (!isKeyPresent(token, userId)) {
            redisTemplate.opsForValue().set(createRedisKey(token, userId), cacheInfo, expirationTime, TimeUnit.SECONDS);
        }
    }

    public <T> T getInfoFromCache(String token, String userId, Class<T> clazz) {
        Object cachedObject = redisTemplate.opsForValue().get(createRedisKey(token, userId));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String metaDataJson = objectMapper.writeValueAsString(cachedObject);

            // Deserializa el JSON de metaData a UserCacheInfo
            return objectMapper.readValue(metaDataJson, clazz);

        } catch (JsonProcessingException e) {
            throw new CustomTemplateException("Error parsing cached value to Class object", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isKeyPresent(String token, String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(createRedisKey(token, userId)));
    }

    private String createRedisKey(String token, String userId) {
        return userId + ":" + token;
    }


}