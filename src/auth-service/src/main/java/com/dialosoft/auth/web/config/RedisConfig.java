package com.dialosoft.auth.web.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Optional;

@Slf4j
@Configuration
@Data
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public Optional<RedisConnectionFactory> jedisConnectionFactory() {
        try {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(host);
            redisStandaloneConfiguration.setPort(port);

            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
            jedisConnectionFactory.afterPropertiesSet();

            if (isRedisAvailable(jedisConnectionFactory)) {
                log.info("Connected to Redis server at {}:{}", host, port);
                return Optional.of(jedisConnectionFactory);
            } else {
                log.warn("Redis server at {}:{} is not available. The application will continue without Redis.", host, port);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Failed to connect to Redis server at {}:{}: {}", host, port, e.getMessage());
            return Optional.empty();
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(Optional<RedisConnectionFactory> redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        redisConnectionFactory.ifPresent(factory -> {
            template.setConnectionFactory(factory);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(new JdkSerializationRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new JdkSerializationRedisSerializer());
            template.afterPropertiesSet();
        });
        if (redisConnectionFactory.isEmpty()) {
            log.warn("RedisTemplate is being created without a connection factory. Redis operations won't be available.");
        }
        return template;
    }

    private boolean isRedisAvailable(JedisConnectionFactory jedisConnectionFactory) {
        try {
            return jedisConnectionFactory.getConnection().ping() != null;
        } catch (Exception e) {
            log.error("Redis ping failed: {}", e.getMessage());
            return false;
        }
    }
}