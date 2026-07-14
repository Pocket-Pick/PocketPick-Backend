package com.pocketpick.chat.presentation.websocket;

import tools.jackson.databind.ObjectMapper;
import com.pocketpick.chat.domain.message.MessageUseCase;
import com.pocketpick.chat.domain.message.dto.WebSocketFrame;
import com.pocketpick.chat.domain.message.dto.WebSocketFrameType;
import com.pocketpick.chat.global.config.ChatServerProperties;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final String USER_ID_ATTRIBUTE = "userId";

    private final WebSocketSessionRegistry sessionRegistry;
    private final OnlineStatusRepository onlineStatusRepository;
    private final MessageUseCase messageUseCase;
    private final ObjectMapper objectMapper;
    private final ChatServerProperties chatServerProperties;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = extractUserId(session);
        sessionRegistry.register(userId, session);
        onlineStatusRepository.markOnline(userId, chatServerProperties.getIp());
        log.info("WebSocket connected: userId={}", userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = extractUserId(session);
        WebSocketFrame frame = objectMapper.readValue(message.getPayload(), WebSocketFrame.class);

        if (frame.getFrameType() == WebSocketFrameType.ENTER_ROOM) {
            sessionRegistry.setCurrentRoom(userId, frame.getRoomId());
            messageUseCase.markAsRead(frame.getRoomId(), userId);
        } else {
            messageUseCase.send(userId, frame);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = extractUserId(session);
        sessionRegistry.remove(userId);
        onlineStatusRepository.markOffline(userId);
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
