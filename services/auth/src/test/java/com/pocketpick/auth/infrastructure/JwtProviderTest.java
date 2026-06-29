package com.pocketpick.auth.infrastructure;

import com.pocketpick.auth.infrastructure.jwt.JwtProperties;
import com.pocketpick.auth.infrastructure.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtProvider")
class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties(
                "test-secret-key-for-testing-purpose-only-32bytes",
                1800000L,
                604800000L
        );
        jwtProvider = new JwtProvider(properties);
    }

    @Nested
    @DisplayName("액세스 토큰 생성")
    class CreateAccessToken {

        @Test
        @DisplayName("tokenType이 access인 토큰을 생성한다")
        void createAccessToken_containsAccessType() {
            String token = jwtProvider.createAccessToken(1L);

            jwtProvider.validateTokenType(token, JwtProvider.TOKEN_TYPE_ACCESS);
        }

        @Test
        @DisplayName("userId가 올바르게 담긴다")
        void createAccessToken_containsUserId() {
            String token = jwtProvider.createAccessToken(1L);

            assertThat(jwtProvider.getUserId(token)).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 생성")
    class CreateRefreshToken {

        @Test
        @DisplayName("tokenType이 refresh인 토큰을 생성한다")
        void createRefreshToken_containsRefreshType() {
            String token = jwtProvider.createRefreshToken(1L);

            jwtProvider.validateTokenType(token, JwtProvider.TOKEN_TYPE_REFRESH);
        }

        @Test
        @DisplayName("userId가 올바르게 담긴다")
        void createRefreshToken_containsUserId() {
            String token = jwtProvider.createRefreshToken(1L);

            assertThat(jwtProvider.getUserId(token)).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("JTI 추출")
    class GetJti {

        @Test
        @DisplayName("생성된 토큰마다 고유한 JTI를 가진다")
        void getJti_eachTokenHasUniqueJti() {
            String token1 = jwtProvider.createAccessToken(1L);
            String token2 = jwtProvider.createAccessToken(1L);

            assertThat(jwtProvider.getJti(token1)).isNotEqualTo(jwtProvider.getJti(token2));
        }

        @Test
        @DisplayName("JTI가 null이 아니다")
        void getJti_returnsNonNull() {
            String token = jwtProvider.createAccessToken(1L);

            assertThat(jwtProvider.getJti(token)).isNotNull();
        }
    }

    @Nested
    @DisplayName("토큰 타입 검증")
    class ValidateTokenType {

        @Test
        @DisplayName("액세스 토큰을 refresh 타입으로 검증하면 예외를 던진다")
        void validateTokenType_accessTokenAsRefresh_throwsException() {
            String accessToken = jwtProvider.createAccessToken(1L);

            assertThatThrownBy(() -> jwtProvider.validateTokenType(accessToken, JwtProvider.TOKEN_TYPE_REFRESH))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("리프레시 토큰을 access 타입으로 검증하면 예외를 던진다")
        void validateTokenType_refreshTokenAsAccess_throwsException() {
            String refreshToken = jwtProvider.createRefreshToken(1L);

            assertThatThrownBy(() -> jwtProvider.validateTokenType(refreshToken, JwtProvider.TOKEN_TYPE_ACCESS))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
