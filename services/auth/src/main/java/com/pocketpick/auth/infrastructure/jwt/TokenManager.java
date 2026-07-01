package com.pocketpick.auth.infrastructure.jwt;

import com.pocketpick.auth.domain.domain.exception.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate redisTemplate;

    public String[] createTokens(Long userId) {
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + userId,
                refreshToken,
                Duration.ofMillis(jwtProperties.getRefreshExpiration())
        );

        return new String[]{accessToken, refreshToken};
    }

    public String[] reissueTokens(String refreshToken) {
        Long userId;
        try {
            userId = jwtProvider.getUserId(refreshToken);
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
        String stored = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);

        if (!refreshToken.equals(stored)) {
            throw new InvalidTokenException();
        }

        return createTokens(userId);
    }

    public void deleteTokens(Long userId, String accessToken) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
        String jti = jwtProvider.getJti(accessToken);
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + jti,
                "logout",
                Duration.ofMillis(jwtProperties.getAccessExpiration())
        );
    }
}
