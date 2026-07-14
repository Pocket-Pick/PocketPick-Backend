package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.message.dto.ChatMessageResponse;
import com.pocketpick.chat.infrastructure.fcm.FcmPushService;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.socket.TextMessage;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDeliveryService {

    private final WebSocketSessionRegistry sessionRegistry;
    private final OnlineStatusRepository onlineStatusRepository;
    private final FcmPushService fcmPushService;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    @Value("${server.port}")
    private int serverPort;

    @Value("${chat.server.ip}")
    private String serverIp;

    public void deliver(ChatMessageEvent event) {
        Long receiverId = event.getReceiverId();

        if (!onlineStatusRepository.isOnline(receiverId)) {
            fcmPushService.sendPush(receiverId, event.getContent());
            return;
        }

        if (sessionRegistry.getSession(receiverId).isPresent()) {
            sendViaWebSocket(receiverId, event);
        } else {
            forwardToTargetServer(receiverId, event);
        }
    }

    private void sendViaWebSocket(Long receiverId, ChatMessageEvent event) {
        sessionRegistry.getSession(receiverId).ifPresent(session -> {
            try {
                ChatMessageResponse response = ChatMessageResponse.from(event);
                String payload = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.error("WebSocket push failed, fallback to FCM: receiverId={}", receiverId, e);
                onlineStatusRepository.markOffline(receiverId);
                fcmPushService.sendPush(receiverId, event.getContent());
            }
        });
    }

    private void forwardToTargetServer(Long receiverId, ChatMessageEvent event) {
        String targetIp = onlineStatusRepository.getServerIp(receiverId);
        if (targetIp == null) {
            fcmPushService.sendPush(receiverId, event.getContent());
            return;
        }

        try {
            restClient.post()
                    .uri("http://" + targetIp + ":" + serverPort + "/internal/messages/deliver")
                    .body(event)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Forward to target server failed: receiverId={}, targetIp={}", receiverId, targetIp, e);
            fcmPushService.sendPush(receiverId, event.getContent());
        }
    }
}
