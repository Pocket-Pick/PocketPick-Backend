package com.pocketpick.chat.presentation.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final String USER_ID_ATTRIBUTE = "userId";

    private final WebSocketSessionRegistry sessionRegistry;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = extractUserId(session);
        sessionRegistry.register(userId, session);
        log.info("WebSocket connected: userId={}", userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = extractUserId(session);
        sessionRegistry.remove(userId);
        log.info("WebSocket disconnected: userId={}, status={}", userId, status);
    }

    private Long extractUserId(WebSocketSession session) {
        Object userId = session.getAttributes().get(USER_ID_ATTRIBUTE);
        if (userId == null) {
            throw new IllegalStateException("userId not found in WebSocket session attributes");
        }
        return Long.parseLong(userId.toString());
    }
}
