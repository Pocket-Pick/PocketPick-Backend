package com.pocketpick.auth.infrastructure;

import com.pocketpick.auth.domain.domain.exception.InvalidTokenException;
import com.pocketpick.auth.infrastructure.jwt.JwtProperties;
import com.pocketpick.auth.infrastructure.jwt.JwtProvider;
import com.pocketpick.auth.infrastructure.jwt.TokenManager;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("TokenManager")
@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private StringRedisTemplate redisTemplate;

    @InjectMocks
    private TokenManager tokenManager;

    @Nested
    @DisplayName("토큰 재발급")
    class ReissueTokens {

        @Test
        @DisplayName("만료/변조된 refreshToken이면 InvalidTokenException을 던진다")
        void reissueTokens_expiredToken_throwsInvalidTokenException() {
            // given
            String expiredToken = "expired.refresh.token";
            given(jwtProvider.getUserId(expiredToken)).willThrow(new JwtException("expired"));

            // when & then
            assertThatThrownBy(() -> tokenManager.reissueTokens(expiredToken))
                    .isInstanceOf(InvalidTokenException.class);
        }

        @Test
        @DisplayName("Redis에 저장된 토큰과 일치하지 않으면 InvalidTokenException을 던진다")
        void reissueTokens_tokenMismatch_throwsInvalidTokenException() {
            // given
            String refreshToken = "valid.but.mismatched.token";
            ValueOperations<String, String> valueOps = mock(ValueOperations.class);
            given(jwtProvider.getUserId(refreshToken)).willReturn(1L);
            given(redisTemplate.opsForValue()).willReturn(valueOps);
            given(valueOps.get("refresh:1")).willReturn("different.token");

            // when & then
            assertThatThrownBy(() -> tokenManager.reissueTokens(refreshToken))
                    .isInstanceOf(InvalidTokenException.class);
        }
    }
}
