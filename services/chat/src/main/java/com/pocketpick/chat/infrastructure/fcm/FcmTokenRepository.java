package com.pocketpick.chat.infrastructure.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FcmTokenRepository {

    private static final String KEY_PREFIX = "user:fcm:";

    private final StringRedisTemplate redisTemplate;

    public void save(Long userId, String fcmToken) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userId, fcmToken);
    }

    public Optional<String> find(Long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + userId));
    }

    public void delete(Long userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }
}
