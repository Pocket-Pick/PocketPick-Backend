package com.pocketpick.chat.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class OnlineStatusRepository {

    private static final String KEY_PREFIX = "user:server:";
    private static final long TTL_MINUTES = 30;

    private final StringRedisTemplate redisTemplate;

    public void markOnline(Long userId, String serverIp) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userId, serverIp, TTL_MINUTES, TimeUnit.MINUTES);
    }

    public void markOffline(Long userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }

    public boolean isOnline(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + userId));
    }

    public String getServerIp(Long userId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + userId);
    }
}
