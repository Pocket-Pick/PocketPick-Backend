package com.pocketpick.chat.global.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class UserIdHandshakeInterceptor implements HandshakeInterceptor {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ID_ATTRIBUTE = "userId";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String userId = request.getHeaders().getFirst(USER_ID_HEADER);
        if (userId == null) {
            return false;
        }
        attributes.put(USER_ID_ATTRIBUTE, userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
