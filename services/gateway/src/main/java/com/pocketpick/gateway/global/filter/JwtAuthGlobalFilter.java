package com.pocketpick.gateway.global.filter;

import com.pocketpick.gateway.infrastructure.jwt.JwtProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String ACCESS_TOKEN_COOKIE = "accessToken";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final List<String> WHITE_LIST = List.of(
            "/api/auth/login",
            "/api/auth/reissue",
            "/api/users"
    );
    private static final List<String> WHITE_LIST_PREFIX = List.of(
            "/api/cards"
    );

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        HttpCookie cookie = exchange.getRequest().getCookies().getFirst(ACCESS_TOKEN_COOKIE);
        if (cookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            Long userId = jwtProvider.getUserId(cookie.getValue());

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header(USER_ID_HEADER, String.valueOf(userId))
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (JwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isWhitelisted(String path) {
        boolean exactMatch = WHITE_LIST.stream().anyMatch(path::equals);
        boolean prefixMatch = WHITE_LIST_PREFIX.stream().anyMatch(path::startsWith);
        return exactMatch || prefixMatch;
    }
}
