package com.dialosoft.auth.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AtomicBoolean redisAvailable = new AtomicBoolean(false);
    private static final int OPERATION_TIMEOUT_MS = 3000;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        checkRedisAvailability();
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }

    @Scheduled(fixedDelay = 8000)
    public void scheduleRedisCheck() {
        checkRedisAvailability();
        log.info("Redis availability check completed, redisAvailable is: {}", isRedisAvailable());
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
        return redisAvailable.get();
    }

    private void checkRedisAvailability() {
        if (Objects.isNull(redisTemplate.getConnectionFactory())) {
            log.warn("Redis connection factory is null");
            redisAvailable.set(false);
            return;
        }

        try {
            CompletableFuture<Boolean> availabilityFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    log.debug("Attempting to get Redis connection...");
                    RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
                    log.debug("Got Redis connection, attempting to ping...");
                    boolean pingSuccessful = connection.ping() != null;
                    log.debug("Redis ping result: {}", pingSuccessful);
                    connection.close();
                    return pingSuccessful;
                } catch (Exception e) {
                    log.error("Error during Redis connection or ping: {}", e.getMessage());
                    return false;
                }
            }, executorService);

            boolean pingSuccessful = availabilityFuture.get(OPERATION_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            if (pingSuccessful && !redisAvailable.get()) {
                log.info("Redis is now available. Reconnecting...");
                redisAvailable.set(true);
            } else if (!pingSuccessful && redisAvailable.get()) {
                log.warn("Redis is no longer available. Disconnecting...");
                redisAvailable.set(false);
            }

        } catch (TimeoutException e) {
            log.warn("Redis operation timed out after {} ms", OPERATION_TIMEOUT_MS);
            redisAvailable.set(false);
        } catch (Exception e) {
            log.error("Unexpected error checking Redis availability: {}", e.getMessage());
            redisAvailable.set(false);
        }
    }
}
