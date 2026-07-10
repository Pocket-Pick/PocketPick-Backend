package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.infrastructure.fcm.FcmPushUseCase;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import com.pocketpick.chat.infrastructure.websocket.WebSocketMessageSender;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock private WebSocketMessageSender webSocketMessageSender;
    @Mock private WebSocketSessionRegistry sessionRegistry;
    @Mock private OnlineStatusRepository onlineStatusRepository;
    @Mock private FcmPushUseCase fcmPushUseCase;
    @Mock private MessageService messageService;

    @InjectMocks
    private MessageDeliveryService messageDeliveryService;

    @Nested
    @DisplayName("메시지 전달")
    class Deliver {

        @Test
        @DisplayName("수신자가 온라인이면 WebSocket으로 전달한다")
        void deliver_receiverOnline_sendsViaWebSocket() {
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(true);
            given(sessionRegistry.getCurrentRoom(2L)).willReturn(Optional.empty());

            messageDeliveryService.deliver(event);

            verify(webSocketMessageSender).send(2L, event);
            verify(fcmPushUseCase, never()).sendPush(anyLong(), anyString());
        }

        @Test
        @DisplayName("수신자가 온라인이고 같은 방에 있으면 즉시 읽음 처리한다")
        void deliver_receiverOnlineInSameRoom_marksAsRead() {
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(true);
            given(sessionRegistry.getCurrentRoom(2L)).willReturn(Optional.of("room-1"));

            messageDeliveryService.deliver(event);

            verify(messageService).markAsRead("room-1", 2L);
        }

        @Test
        @DisplayName("수신자가 오프라인이면 FCM으로 전달한다")
        void deliver_receiverOffline_sendsFcm() {
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(false);

            messageDeliveryService.deliver(event);

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
