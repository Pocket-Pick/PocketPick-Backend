package com.pocketpick.chat.infrastructure.websocket;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.message.dto.ChatMessageResponse;
import com.pocketpick.chat.domain.message.dto.ReadEvent;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketMessageSender {

    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public void send(Long receiverId, ChatMessageEvent event) {
        sessionRegistry.getSession(receiverId).ifPresent(session -> {
            try {
                ChatMessageResponse response = ChatMessageResponse.from(event);
                String payload = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.error("WebSocket push failed: receiverId={}", receiverId, e);
                throw new UncheckedIOException(e);
            }
        });
    }

    public void sendReadEvent(Long userId, ReadEvent event) {
        sessionRegistry.getSession(userId).ifPresent(session -> {
            try {
                String payload = objectMapper.writeValueAsString(event);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.error("WebSocket read event push failed: userId={}", userId, e);
            }
        });
    }
}
