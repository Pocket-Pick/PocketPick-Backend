package com.pocketpick.auth.interceptor;

import com.pocketpick.auth.global.config.InternalApiKeyProperties;
import com.pocketpick.auth.global.exception.UnauthorizedInternalRequestException;
import com.pocketpick.auth.global.interceptor.InternalApiKeyInterceptor;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("InternalApiKeyInterceptor")
@ExtendWith(MockitoExtension.class)
class InternalApiKeyInterceptorTest {

    private static final String VALID_API_KEY = "test-internal-api-key";

    private InternalApiKeyInterceptor interceptor;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        InternalApiKeyProperties properties = new InternalApiKeyProperties();
        properties.setApiKey(VALID_API_KEY);
        interceptor = new InternalApiKeyInterceptor(properties);
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("preHandle")
    class PreHandle {

        @Test
        @DisplayName("올바른 API Key면 true를 반환한다")
        void preHandle_validApiKey_returnsTrue() {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("X-Internal-Api-Key", VALID_API_KEY);

            // when
            boolean result = interceptor.preHandle(request, response, new Object());

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("API Key 헤더가 없으면 UnauthorizedInternalRequestException을 던진다")
        void preHandle_missingApiKey_throwsUnauthorizedInternalRequestException() {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();

            // when & then
            assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                    .isInstanceOf(UnauthorizedInternalRequestException.class);
        }

        @Test
        @DisplayName("API Key가 틀리면 UnauthorizedInternalRequestException을 던진다")
        void preHandle_wrongApiKey_throwsUnauthorizedInternalRequestException() {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("X-Internal-Api-Key", "wrong-key");

            // when & then
            assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                    .isInstanceOf(UnauthorizedInternalRequestException.class);
        }
    }
}
