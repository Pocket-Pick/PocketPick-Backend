package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.infrastructure.fcm.FcmPushUseCase;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("MessageDeliveryService")
@ExtendWith(MockitoExtension.class)
class MessageDeliveryServiceTest {

    @Mock
    private WebSocketSessionRegistry sessionRegistry;

    @Mock
    private OnlineStatusRepository onlineStatusRepository;

    @Mock
    private FcmPushUseCase fcmPushUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WebSocketSession webSocketSession;

    @InjectMocks
    private MessageDeliveryService messageDeliveryService;

    @Nested
    @DisplayName("메시지 전달")
    class Deliver {

        @Test
        @DisplayName("수신자가 온라인이면 WebSocket으로 전달한다")
        void deliver_receiverOnline_sendsViaWebSocket() throws Exception {
            // given
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(true);
            given(sessionRegistry.getSession(2L)).willReturn(Optional.of(webSocketSession));
            given(objectMapper.writeValueAsString(any())).willReturn("{\"messageId\":\"1\"}");

            // when
            messageDeliveryService.deliver(event);

            // then
            verify(webSocketSession).sendMessage(any());
            verify(fcmPushUseCase, never()).sendPush(anyLong(), anyString());
        }

        @Test
        @DisplayName("수신자가 오프라인이면 FCM으로 전달한다")
        void deliver_receiverOffline_sendsVisFcm() {
            // given
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(false);

            // when
            messageDeliveryService.deliver(event);

            // then
            verify(fcmPushUseCase).sendPush(2L, "안녕하세요");
            verify(sessionRegistry, never()).getSession(anyLong());
        }
    }

    private ChatMessageEvent createEvent(Long senderId, Long receiverId) {
        return new ChatMessageEvent("msg-1", "room-1", senderId, receiverId, "안녕하세요", MessageType.TEXT, LocalDateTime.now());
    }
}
