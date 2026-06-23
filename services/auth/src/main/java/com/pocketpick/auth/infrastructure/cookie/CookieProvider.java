package com.pocketpick.auth.infrastructure.cookie;

import com.pocketpick.auth.infrastructure.jwt.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final JwtProperties jwtProperties;

    public void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        addCookie(response, "accessToken", accessToken, (int) (jwtProperties.getAccessExpiration() / 1000));
        addCookie(response, "refreshToken", refreshToken, (int) (jwtProperties.getRefreshExpiration() / 1000));
    }

    public void expireTokenCookies(HttpServletResponse response) {
        expireCookie(response, "accessToken");
        expireCookie(response, "refreshToken");
    }

    private void expireCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
