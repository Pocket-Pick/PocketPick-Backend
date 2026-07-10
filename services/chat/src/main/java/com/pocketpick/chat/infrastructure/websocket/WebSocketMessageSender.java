package com.pocketpick.chat.infrastructure.websocket;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.message.dto.ChatMessageResponse;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketMessageSender {

    private final WebSocketSessionRegistry sessionRegistry;
    private final OnlineStatusRepository onlineStatusRepository;
    private final ObjectMapper objectMapper;

    public void send(Long receiverId, ChatMessageEvent event) {
        sessionRegistry.getSession(receiverId).ifPresent(session -> {
            try {
                ChatMessageResponse response = ChatMessageResponse.from(event);
                String payload = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.error("WebSocket push failed: receiverId={}", receiverId, e);
                onlineStatusRepository.markOffline(receiverId);
            }
        });
    }
}
