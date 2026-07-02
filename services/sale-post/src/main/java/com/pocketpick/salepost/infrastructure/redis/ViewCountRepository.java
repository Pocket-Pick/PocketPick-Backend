package com.pocketpick.salepost.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ViewCountRepository {

    private static final String KEY_PREFIX = "sale-post:view:";

    private final RedisTemplate<String, Long> redisTemplate;

    public void increment(Long salePostId) {
        redisTemplate.opsForValue().increment(KEY_PREFIX + salePostId);
    }

    public Long get(Long salePostId) {
        Long value = redisTemplate.opsForValue().get(KEY_PREFIX + salePostId);
        return value != null ? value : 0L;
    }

    public Set<String> getAllKeys() {
        return redisTemplate.keys(KEY_PREFIX + "*");
    }

    public void delete(Long salePostId) {
        redisTemplate.delete(KEY_PREFIX + salePostId);
    }

    public Long extractSalePostId(String key) {
        return Long.parseLong(key.substring(KEY_PREFIX.length()));
    }
}
