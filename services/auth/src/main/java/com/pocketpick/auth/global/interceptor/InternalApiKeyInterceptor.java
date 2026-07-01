package com.pocketpick.auth.global.interceptor;

import com.pocketpick.auth.global.config.InternalApiKeyProperties;
import com.pocketpick.auth.global.exception.UnauthorizedInternalRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class InternalApiKeyInterceptor implements HandlerInterceptor {

    private static final String HEADER_NAME = "X-Internal-Api-Key";

    private final InternalApiKeyProperties internalApiKeyProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String key = request.getHeader(HEADER_NAME);
        if (key == null || !key.equals(internalApiKeyProperties.getApiKey())) {
            throw new UnauthorizedInternalRequestException();
        }
        return true;
    }
}
