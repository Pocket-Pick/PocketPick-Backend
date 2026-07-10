package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.infrastructure.fcm.FcmPushUseCase;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import com.pocketpick.chat.infrastructure.websocket.WebSocketMessageSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
    private WebSocketMessageSender webSocketMessageSender;

    @Mock
    private OnlineStatusRepository onlineStatusRepository;

    @Mock
    private FcmPushUseCase fcmPushUseCase;

    @InjectMocks
    private MessageDeliveryService messageDeliveryService;

    @Nested
    @DisplayName("메시지 전달")
    class Deliver {

        @Test
        @DisplayName("수신자가 온라인이면 WebSocket으로 전달한다")
        void deliver_receiverOnline_sendsViaWebSocket() {
            // given
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(true);

            // when
            messageDeliveryService.deliver(event);

            // then
            verify(webSocketMessageSender).send(2L, event);
            verify(fcmPushUseCase, never()).sendPush(anyLong(), anyString());
        }

        @Test
        @DisplayName("수신자가 오프라인이면 FCM으로 전달한다")
        void deliver_receiverOffline_sendsFcm() {
            // given
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(false);

            // when
            messageDeliveryService.deliver(event);

            // then
            verify(fcmPushUseCase).sendPush(2L, "안녕하세요");
            verify(webSocketMessageSender, never()).send(anyLong(), any());
        }
    }

    private ChatMessageEvent createEvent(Long senderId, Long receiverId) {
        return ChatMessageEvent.builder()
                .messageId("msg-1")
                .roomId("room-1")
                .senderId(senderId)
                .receiverId(receiverId)
                .content("안녕하세요")
                .type(MessageType.TEXT)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
