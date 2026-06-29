package com.pocketpick.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";
    private static final String CLAIM_USER_ID = "userId";

    private final JwtProperties jwtProperties;

    public String createAccessToken(Long userId) {
        return createToken(userId, TOKEN_TYPE_ACCESS, jwtProperties.getAccessExpiration());
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, TOKEN_TYPE_REFRESH, jwtProperties.getRefreshExpiration());
    }

    public Long getUserId(String token) {
        return parseClaims(token).get(CLAIM_USER_ID, Long.class);
    }

    public void validateTokenType(String token, String expectedType) {
        String actualType = parseClaims(token).get(CLAIM_TOKEN_TYPE, String.class);
        if (!expectedType.equals(actualType)) {
            throw new IllegalArgumentException("토큰 타입이 올바르지 않습니다. expected=" + expectedType + ", actual=" + actualType);
        }
    }

    private String createToken(Long userId, String tokenType, long expiration) {
        Date now = new Date();
        return Jwts.builder()
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_TOKEN_TYPE, tokenType)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(secretKey())
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
