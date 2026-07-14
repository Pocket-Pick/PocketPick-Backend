package com.pocketpick.chat.presentation.internal;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.message.dto.ChatMessageResponse;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/internal/messages")
@RequiredArgsConstructor
public class InternalMessageController {

    private final WebSocketSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    @PostMapping("/deliver")
    public ResponseEntity<Void> deliver(@RequestBody ChatMessageEvent event) {
        Long receiverId = event.getReceiverId();
        sessionRegistry.getSession(receiverId).ifPresent(session -> {
            try {
                ChatMessageResponse response = ChatMessageResponse.from(event);
                String payload = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.error("Internal WebSocket deliver failed: receiverId={}", receiverId, e);
            }
        });
        return ResponseEntity.ok().build();
    }
}
