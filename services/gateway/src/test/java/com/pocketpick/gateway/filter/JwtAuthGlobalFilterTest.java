package com.pocketpick.gateway.filter;

import com.pocketpick.gateway.global.filter.JwtAuthGlobalFilter;
import com.pocketpick.gateway.infrastructure.jwt.JwtProvider;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("JwtAuthGlobalFilter")
@ExtendWith(MockitoExtension.class)
class JwtAuthGlobalFilterTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private GatewayFilterChain filterChain;

    @InjectMocks
    private JwtAuthGlobalFilter filter;

    @Nested
    @DisplayName("화이트리스트 경로")
    class Whitelist {

        @Test
        @DisplayName("로그인 경로는 토큰 없이 통과한다")
        void filter_loginPath_passesWithoutToken() {
            // given
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.post("/api/auth/login").build()
            );
            given(filterChain.filter(any())).willReturn(Mono.empty());

            // when & then
            StepVerifier.create(filter.filter(exchange, filterChain))
                    .verifyComplete();

            assertThat(exchange.getResponse().getStatusCode()).isNull();
        }

        @Test
        @DisplayName("토큰 재발급 경로는 토큰 없이 통과한다")
        void filter_reissuePath_passesWithoutToken() {
            // given
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.post("/api/auth/reissue").build()
            );
            given(filterChain.filter(any())).willReturn(Mono.empty());

            // when & then
            StepVerifier.create(filter.filter(exchange, filterChain))
                    .verifyComplete();

            assertThat(exchange.getResponse().getStatusCode()).isNull();
        }

        @Test
        @DisplayName("회원가입 경로는 토큰 없이 통과한다")
        void filter_signupPath_passesWithoutToken() {
            // given
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.post("/api/users").build()
            );
            given(filterChain.filter(any())).willReturn(Mono.empty());

            // when & then
            StepVerifier.create(filter.filter(exchange, filterChain))
                    .verifyComplete();

            assertThat(exchange.getResponse().getStatusCode()).isNull();
        }

        @Test
        @DisplayName("카드 조회 경로는 토큰 없이 통과한다")
        void filter_cardsPath_passesWithoutToken() {
            // given
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/cards").build()
            );
            given(filterChain.filter(any())).willReturn(Mono.empty());

            // when & then
            StepVerifier.create(filter.filter(exchange, filterChain))
                    .verifyComplete();

            assertThat(exchange.getResponse().getStatusCode()).isNull();
        }
    }

    @Nested
    @DisplayName("인증 필요 경로")
    class AuthRequired {

        @Test
        @DisplayName("accessToken 쿠키가 없으면 401을 반환한다")
        void filter_noToken_returns401() {
            // given
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users/1").build()
            );

            // when & then
            StepVerifier.create(filter.filter(exchange, filterChain))
                    .verifyComplete();

            assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("유효하지 않은 토큰이면 401을 반환한다")
        void filter_invalidToken_returns401() {
            // given
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users/1")
                            .cookie(new HttpCookie("accessToken", "invalid-token"))
                            .build()
            );
            given(jwtProvider.getUserId("invalid-token")).willThrow(new JwtException("invalid"));

            // when & then
            StepVerifier.create(filter.filter(exchange, filterChain))
                    .verifyComplete();

            assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("유효한 토큰이면 X-User-Id 헤더를 추가하고 통과한다")
        void filter_validToken_addsUserIdHeaderAndPasses() {
            // given
            given(jwtProvider.getUserId("valid-token")).willReturn(1L);
            given(filterChain.filter(any())).willAnswer(invocation -> {
                ServerWebExchange mutatedExchange = invocation.getArgument(0);
                assertThat(mutatedExchange.getRequest().getHeaders().getFirst("X-User-Id")).isEqualTo("1");
                return Mono.empty();
            });

            MockServerWebExchange exchange = MockServerWebExchange.from(
                    MockServerHttpRequest.get("/api/users/1")
                            .cookie(new HttpCookie("accessToken", "valid-token"))
                            .build()
            );

            // when & then
            StepVerifier.create(filter.filter(exchange, filterChain))
                    .verifyComplete();
        }
    }
}
