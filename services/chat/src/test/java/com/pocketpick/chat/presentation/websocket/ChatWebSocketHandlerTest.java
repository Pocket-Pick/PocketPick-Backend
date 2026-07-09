package com.pocketpick.chat.presentation.websocket;

import com.pocketpick.chat.application.MessageService;
import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.SendMessageRequest;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("ChatWebSocketHandler")
@ExtendWith(MockitoExtension.class)
class ChatWebSocketHandlerTest {

    @Mock
    private WebSocketSessionRegistry sessionRegistry;

    @Mock
    private OnlineStatusRepository onlineStatusRepository;

    @Mock
    private MessageService messageService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WebSocketSession session;

    @InjectMocks
    private ChatWebSocketHandler handler;

    private final Map<String, Object> attributes = new HashMap<>();

    @BeforeEach
    void setUp() {
        attributes.put("userId", "42");
        given(session.getAttributes()).willReturn(attributes);
    }

    @Nested
    @DisplayName("연결")
    class Connection {

        @Test
        @DisplayName("연결 시 세션 등록과 Redis 온라인 표시를 한다")
        void afterConnectionEstablished_registersSessionAndMarksOnline() {
            // when
            handler.afterConnectionEstablished(session);

            // then
            verify(sessionRegistry).register(42L, session);
            verify(onlineStatusRepository).markOnline(42L);
        }

        @Test
        @DisplayName("해제 시 세션 제거와 Redis 오프라인 표시를 한다")
        void afterConnectionClosed_removesSessionAndMarksOffline() {
            // when
            handler.afterConnectionClosed(session, CloseStatus.NORMAL);

            // then
            verify(sessionRegistry).remove(42L);
            verify(onlineStatusRepository).markOffline(42L);
        }
    }

    @Nested
    @DisplayName("메시지 처리")
    class MessageHandling {

        @Test
        @DisplayName("텍스트 메시지 수신 시 MessageService로 위임한다")
        void handleTextMessage_delegatesToMessageService() throws Exception {
            // given
            String json = "{\"roomId\":\"room-1\",\"receiverId\":2,\"content\":\"안녕\",\"type\":\"TEXT\"}";
            SendMessageRequest request = new SendMessageRequest("room-1", 2L, "안녕", MessageType.TEXT);
            given(objectMapper.readValue(eq(json), eq(SendMessageRequest.class))).willReturn(request);

            // when
            handler.handleMessage(session, new TextMessage(json));

            // then
            verify(messageService).send(42L, request);
        }
    }
}
