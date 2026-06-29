package com.pocketpick.auth.controller;

import tools.jackson.databind.ObjectMapper;
import com.pocketpick.auth.domain.controller.InternalAccountController;
import com.pocketpick.auth.domain.domain.exception.DuplicateEmailException;
import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;
import com.pocketpick.auth.domain.dto.ValidateCredentialsRequest;
import com.pocketpick.auth.domain.service.AccountUseCase;
import com.pocketpick.auth.global.config.InternalApiKeyProperties;
import com.pocketpick.auth.global.exception.GlobalExceptionHandler;
import com.pocketpick.auth.global.interceptor.InternalApiKeyInterceptor;
import com.pocketpick.auth.support.fixture.AccountFixture;
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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("InternalAccountController")
@ExtendWith(MockitoExtension.class)
class InternalAccountControllerTest {

    private static final String VALID_API_KEY = "test-internal-api-key";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AccountUseCase accountUseCase;

    @InjectMocks
    private InternalAccountController internalAccountController;

    @BeforeEach
    void setUp() {
        InternalApiKeyProperties properties = new InternalApiKeyProperties();
        properties.setApiKey(VALID_API_KEY);
        InternalApiKeyInterceptor interceptor = new InternalApiKeyInterceptor(properties);

        mockMvc = MockMvcBuilders.standaloneSetup(internalAccountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addInterceptors(interceptor)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /internal/accounts/validate")
    class Validate {

        @Test
        @DisplayName("올바른 API Key와 정상 요청이면 200을 반환한다")
        void validate_validKeyAndRequest_returns200() throws Exception {
            // given
            ValidateCredentialsRequest request = new ValidateCredentialsRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            willDoNothing().given(accountUseCase).validate(any());

            // when & then
            mockMvc.perform(post("/internal/accounts/validate")
                            .header("X-Internal-Api-Key", VALID_API_KEY)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("API Key 헤더가 없으면 403을 반환한다")
        void validate_missingApiKey_returns403() throws Exception {
            // given
            ValidateCredentialsRequest request = new ValidateCredentialsRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);

            // when & then
            mockMvc.perform(post("/internal/accounts/validate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED_INTERNAL_REQUEST"));
        }

        @Test
        @DisplayName("이메일이 중복이면 409를 반환한다")
        void validate_duplicateEmail_returns409() throws Exception {
            // given
            ValidateCredentialsRequest request = new ValidateCredentialsRequest(AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            willThrow(new DuplicateEmailException()).given(accountUseCase).validate(any());

            // when & then
            mockMvc.perform(post("/internal/accounts/validate")
                            .header("X-Internal-Api-Key", VALID_API_KEY)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.errorCode").value("DUPLICATE_EMAIL"));
        }
    }

    @Nested
    @DisplayName("POST /internal/accounts/credentials")
    class CreateCredentials {

        @Test
        @DisplayName("올바른 API Key와 정상 요청이면 200을 반환한다")
        void createCredentials_validKeyAndRequest_returns200() throws Exception {
            // given
            CreateCredentialsRequest request = new CreateCredentialsRequest(AccountFixture.USER_ID, AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            willDoNothing().given(accountUseCase).createCredentials(any());

            // when & then
            mockMvc.perform(post("/internal/accounts/credentials")
                            .header("X-Internal-Api-Key", VALID_API_KEY)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("API Key 헤더가 없으면 403을 반환한다")
        void createCredentials_missingApiKey_returns403() throws Exception {
            // given
            CreateCredentialsRequest request = new CreateCredentialsRequest(AccountFixture.USER_ID, AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);

            // when & then
            mockMvc.perform(post("/internal/accounts/credentials")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED_INTERNAL_REQUEST"));
        }
    }
}
