package com.pocketpick.chat.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OnlineStatusRepository {

    private static final String KEY_PREFIX = "user:online:";

    private final StringRedisTemplate redisTemplate;

    public void markOnline(Long userId) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userId, "1");
    }

    public void markOffline(Long userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }

    public boolean isOnline(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + userId));
    }
}
