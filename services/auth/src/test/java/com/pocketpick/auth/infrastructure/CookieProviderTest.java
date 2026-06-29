package com.pocketpick.auth.infrastructure;

import com.pocketpick.auth.domain.domain.exception.MissingTokenException;
import com.pocketpick.auth.infrastructure.cookie.CookieProperties;
import com.pocketpick.auth.infrastructure.cookie.CookieProvider;
import com.pocketpick.auth.infrastructure.jwt.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("CookieProvider")
class CookieProviderTest {

    private CookieProvider cookieProvider;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties(
                "test-secret-key-for-testing-purpose-only-32bytes",
                1800000L,
                604800000L
        );
        CookieProperties cookieProperties = new CookieProperties("Lax", false);
        cookieProvider = new CookieProvider(jwtProperties, cookieProperties);
    }

    @Nested
    @DisplayName("쿠키 추출")
    class ExtractCookie {

        @Test
        @DisplayName("해당 이름의 쿠키가 있으면 값을 반환한다")
        void extractCookie_cookieExists_returnsValue() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            given(request.getCookies()).willReturn(new Cookie[]{new Cookie("accessToken", "tokenValue")});

            String value = cookieProvider.extractCookie(request, "accessToken");

            assertThat(value).isEqualTo("tokenValue");
        }

        @Test
        @DisplayName("쿠키가 없으면 MissingTokenException을 던진다")
        void extractCookie_noCookies_throwsMissingTokenException() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            given(request.getCookies()).willReturn(null);

            assertThatThrownBy(() -> cookieProvider.extractCookie(request, "accessToken"))
                    .isInstanceOf(MissingTokenException.class);
        }

        @Test
        @DisplayName("해당 이름의 쿠키가 없으면 MissingTokenException을 던진다")
        void extractCookie_cookieNameNotFound_throwsMissingTokenException() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            given(request.getCookies()).willReturn(new Cookie[]{new Cookie("otherCookie", "value")});

            assertThatThrownBy(() -> cookieProvider.extractCookie(request, "accessToken"))
                    .isInstanceOf(MissingTokenException.class);
        }
    }
}
