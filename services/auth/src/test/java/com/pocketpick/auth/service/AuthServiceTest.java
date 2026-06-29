package com.pocketpick.auth.service;

import com.pocketpick.auth.domain.domain.exception.AccountNotFoundException;
import com.pocketpick.auth.domain.domain.exception.InvalidPasswordException;
import com.pocketpick.auth.domain.dto.LoginRequest;
import com.pocketpick.auth.domain.repository.AccountRepository;
import com.pocketpick.auth.domain.service.AuthService;
import com.pocketpick.auth.infrastructure.cookie.CookieProvider;
import com.pocketpick.auth.infrastructure.jwt.JwtProvider;
import com.pocketpick.auth.infrastructure.jwt.TokenManager;
import com.pocketpick.auth.support.fixture.AccountFixture;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("AuthService")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private TokenManager tokenManager;
    @Mock
    private CookieProvider cookieProvider;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("로그인")
    class Login {

        @Test
        @DisplayName("정상 요청이면 토큰 쿠키를 설정한다")
        void login_validCredentials_setsCookies() {
            // given
            LoginRequest request = new LoginRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            given(accountRepository.findByEmail(AccountFixture.EMAIL))
                    .willReturn(Optional.of(AccountFixture.account()));
            given(passwordEncoder.matches(AccountFixture.RAW_PASSWORD, AccountFixture.ENCODED_PASSWORD))
                    .willReturn(true);
            given(tokenManager.createTokens(AccountFixture.USER_ID))
                    .willReturn(new String[]{"accessToken", "refreshToken"});
            HttpServletResponse response = mock(HttpServletResponse.class);

            // when & then (예외 없이 정상 실행)
            authService.login(request, response);
        }

        @Test
        @DisplayName("존재하지 않는 이메일이면 AccountNotFoundException을 던진다")
        void login_emailNotFound_throwsAccountNotFoundException() {
            // given
            LoginRequest request = new LoginRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            given(accountRepository.findByEmail(AccountFixture.EMAIL)).willReturn(Optional.empty());
            HttpServletResponse response = mock(HttpServletResponse.class);

            // when & then
            assertThatThrownBy(() -> authService.login(request, response))
                    .isInstanceOf(AccountNotFoundException.class);
        }

        @Test
        @DisplayName("비밀번호 불일치이면 InvalidPasswordException을 던진다")
        void login_wrongPassword_throwsInvalidPasswordException() {
            // given
            LoginRequest request = new LoginRequest(AccountFixture.EMAIL, "wrongPassword");
            given(accountRepository.findByEmail(AccountFixture.EMAIL))
                    .willReturn(Optional.of(AccountFixture.account()));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
            HttpServletResponse response = mock(HttpServletResponse.class);

            // when & then
            assertThatThrownBy(() -> authService.login(request, response))
                    .isInstanceOf(InvalidPasswordException.class);
        }
    }

    @Nested
    @DisplayName("로그아웃")
    class Logout {

        @Test
        @DisplayName("정상 요청이면 토큰을 삭제하고 쿠키를 만료시킨다")
        void logout_validToken_deletesTokensAndExpiresCookies() {
            // given
            String accessToken = "validAccessToken";
            given(jwtProvider.getUserId(accessToken)).willReturn(AccountFixture.USER_ID);
            HttpServletResponse response = mock(HttpServletResponse.class);

            // when
            authService.logout(accessToken, response);

            // then
            verify(tokenManager).deleteTokens(AccountFixture.USER_ID, accessToken);
            verify(cookieProvider).expireTokenCookies(response);
        }

        @Test
        @DisplayName("변조된 토큰이어도 쿠키를 만료시킨다")
        void logout_corruptedToken_stillExpiresCookies() {
            // given
            String corruptedToken = "corrupted.token.value";
            given(jwtProvider.getUserId(corruptedToken)).willThrow(new RuntimeException("invalid token"));
            HttpServletResponse response = mock(HttpServletResponse.class);

            // when
            authService.logout(corruptedToken, response);

            // then
            verify(cookieProvider).expireTokenCookies(response);
        }
    }
}
