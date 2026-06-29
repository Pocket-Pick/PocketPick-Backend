package com.pocketpick.auth.infrastructure.cookie;

import com.pocketpick.auth.domain.domain.exception.MissingTokenException;
import com.pocketpick.auth.infrastructure.jwt.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final JwtProperties jwtProperties;
    private final CookieProperties cookieProperties;

    public String extractCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new MissingTokenException();
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new MissingTokenException();
    }

    public void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader("Set-Cookie", buildCookie("accessToken", accessToken, (int) (jwtProperties.getAccessExpiration() / 1000)).toString());
        response.addHeader("Set-Cookie", buildCookie("refreshToken", refreshToken, (int) (jwtProperties.getRefreshExpiration() / 1000)).toString());
    }

    public void expireTokenCookies(HttpServletResponse response) {
        response.addHeader("Set-Cookie", buildCookie("accessToken", "", 0).toString());
        response.addHeader("Set-Cookie", buildCookie("refreshToken", "", 0).toString());
    }

    private ResponseCookie buildCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieProperties.getSameSite())
                .secure(cookieProperties.isSecure())
                .build();
    }
}
