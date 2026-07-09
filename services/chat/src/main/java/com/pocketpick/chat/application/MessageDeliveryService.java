package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.message.dto.ChatMessageResponse;
import com.pocketpick.chat.infrastructure.fcm.FcmPushService;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDeliveryService {

    private final WebSocketSessionRegistry sessionRegistry;
    private final FcmPushService fcmPushService;
    private final ObjectMapper objectMapper;

    public void deliver(ChatMessageEvent event) {
        Long receiverId = event.getReceiverId();

        if (sessionRegistry.isOnline(receiverId)) {
            sendViaWebSocket(receiverId, event);
        } else {
            fcmPushService.sendPush(receiverId, event.getContent());
        }
    }

    private void sendViaWebSocket(Long receiverId, ChatMessageEvent event) {
        sessionRegistry.getSession(receiverId).ifPresent(session -> {
            try {
                ChatMessageResponse response = ChatMessageResponse.from(event);
                String payload = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.error("WebSocket push failed: receiverId={}", receiverId, e);
            }
        });
    }
}
