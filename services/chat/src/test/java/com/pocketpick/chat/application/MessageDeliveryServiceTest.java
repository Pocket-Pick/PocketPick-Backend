package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.global.config.ChatServerProperties;
import com.pocketpick.chat.infrastructure.fcm.FcmPushService;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
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

    @Mock private WebSocketSessionRegistry sessionRegistry;
    @Mock private OnlineStatusRepository onlineStatusRepository;
    @Mock private FcmPushService fcmPushService;
    @Mock private ObjectMapper objectMapper;
    @Mock(answer = org.mockito.Answers.RETURNS_DEEP_STUBS) private RestClient restClient;
    @Mock private ChatServerProperties chatServerProperties;
    @Mock private WebSocketSession webSocketSession;

    @InjectMocks
    private MessageDeliveryService messageDeliveryService;

    @Nested
    @DisplayName("메시지 전달")
    class Deliver {

        @Test
        @DisplayName("수신자가 오프라인이면 FCM으로 전달한다")
        void deliver_receiverOffline_sendsFcm() {
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(false);

            messageDeliveryService.deliver(event);

            verify(fcmPushService).sendPush(2L, "안녕하세요");
            verify(sessionRegistry, never()).getSession(anyLong());
        }

        @Test
        @DisplayName("수신자가 온라인이고 로컬 세션이 있으면 WebSocket으로 전달한다")
        void deliver_receiverOnlineLocalSession_sendsViaWebSocket() throws Exception {
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(true);
            given(sessionRegistry.getSession(2L)).willReturn(Optional.of(webSocketSession));
            given(objectMapper.writeValueAsString(any())).willReturn("{}");

            messageDeliveryService.deliver(event);

            verify(webSocketSession).sendMessage(any());
            verify(fcmPushService, never()).sendPush(anyLong(), anyString());
        }

        @Test
        @DisplayName("온라인이지만 로컬 세션이 없으면 다른 서버로 포워딩한다")
        void deliver_onlineNoLocalSession_forwardsToTargetServer() {
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(true);
            given(sessionRegistry.getSession(2L)).willReturn(Optional.empty());
            given(onlineStatusRepository.getServerIp(2L)).willReturn("10.0.0.2");
            given(chatServerProperties.getPort()).willReturn(8084);

            messageDeliveryService.deliver(event);

            verify(restClient).post();
            verify(fcmPushService, never()).sendPush(anyLong(), anyString());
        }

        @Test
        @DisplayName("온라인이지만 로컬 세션 없고 서버 IP도 없으면 FCM으로 폴백한다")
        void deliver_onlineNoSessionNoServerIp_sendsFcm() {
            ChatMessageEvent event = createEvent(1L, 2L);
            given(onlineStatusRepository.isOnline(2L)).willReturn(true);
            given(sessionRegistry.getSession(2L)).willReturn(Optional.empty());
            given(onlineStatusRepository.getServerIp(2L)).willReturn(null);

            messageDeliveryService.deliver(event);

            verify(fcmPushService).sendPush(2L, "안녕하세요");
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
