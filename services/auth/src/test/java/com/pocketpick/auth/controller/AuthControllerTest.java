package com.pocketpick.auth.controller;

import tools.jackson.databind.ObjectMapper;
import com.pocketpick.auth.domain.controller.AuthController;
import com.pocketpick.auth.domain.domain.exception.AccountNotFoundException;
import com.pocketpick.auth.domain.domain.exception.InvalidPasswordException;
import com.pocketpick.auth.domain.domain.exception.InvalidTokenException;
import com.pocketpick.auth.domain.dto.LoginRequest;
import com.pocketpick.auth.domain.service.AuthUseCase;
import com.pocketpick.auth.global.exception.GlobalExceptionHandler;
import com.pocketpick.auth.domain.domain.exception.MissingTokenException;
import com.pocketpick.auth.support.fixture.AccountFixture;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AuthController")
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthUseCase authUseCase;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /auth/login")
    class Login {

        @Test
        @DisplayName("정상 요청이면 200을 반환한다")
        void login_validCredentials_returns200() throws Exception {
            // given
            LoginRequest request = new LoginRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            willDoNothing().given(authUseCase).login(any(), any());

            // when & then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 이메일이면 401을 반환한다")
        void login_emailNotFound_returns401() throws Exception {
            // given
            LoginRequest request = new LoginRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            willThrow(new AccountNotFoundException()).given(authUseCase).login(any(), any());

            // when & then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUND"));
        }

        @Test
        @DisplayName("비밀번호가 틀리면 401을 반환한다")
        void login_wrongPassword_returns401() throws Exception {
            // given
            LoginRequest request = new LoginRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            willThrow(new InvalidPasswordException()).given(authUseCase).login(any(), any());

            // when & then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_PASSWORD"));
        }

        @Test
        @DisplayName("이메일이 비어있으면 400을 반환한다")
        void login_blankEmail_returns400() throws Exception {
            // given
            String invalidBody = "{\"email\": \"\", \"password\": \"password123\"}";

            // when & then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT_VALUE"));
        }
    }

    @Nested
    @DisplayName("POST /auth/logout")
    class Logout {

        @Test
        @DisplayName("accessToken 쿠키가 있으면 200을 반환한다")
        void logout_withAccessTokenCookie_returns200() throws Exception {
            // given
            willDoNothing().given(authUseCase).logout(any(), any());

            // when & then
            mockMvc.perform(post("/auth/logout")
                            .cookie(new Cookie("accessToken", "validAccessToken")))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("accessToken 쿠키가 없으면 401을 반환한다")
        void logout_missingAccessTokenCookie_returns401() throws Exception {
            // given
            willThrow(new MissingTokenException()).given(authUseCase).logout(any(), any());

            // when & then
            mockMvc.perform(post("/auth/logout"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("MISSING_TOKEN"));
        }
    }

    @Nested
    @DisplayName("POST /auth/reissue")
    class Reissue {

        @Test
        @DisplayName("refreshToken 쿠키가 있으면 200을 반환한다")
        void reissue_withRefreshTokenCookie_returns200() throws Exception {
            // given
            willDoNothing().given(authUseCase).reissue(any(), any());

            // when & then
            mockMvc.perform(post("/auth/reissue")
                            .cookie(new Cookie("refreshToken", "validRefreshToken")))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("refreshToken 쿠키가 없으면 401을 반환한다")
        void reissue_missingRefreshTokenCookie_returns401() throws Exception {
            // given
            willThrow(new MissingTokenException()).given(authUseCase).reissue(any(), any());

            // when & then
            mockMvc.perform(post("/auth/reissue"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("MISSING_TOKEN"));
        }

        @Test
        @DisplayName("유효하지 않은 refreshToken이면 401을 반환한다")
        void reissue_invalidRefreshToken_returns401() throws Exception {
            // given
            willThrow(new InvalidTokenException()).given(authUseCase).reissue(any(), any());

            // when & then
            mockMvc.perform(post("/auth/reissue")
                            .cookie(new Cookie("refreshToken", "invalidRefreshToken")))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_TOKEN"));
        }
    }
}
