package com.pocketpick.user.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FcmTokenRedisRepository {

    private static final String KEY_PREFIX = "user:fcm:";

    private final StringRedisTemplate redisTemplate;

    public void save(Long userId, String fcmToken) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userId, fcmToken);
    }
}
