package com.pocketpick.chat.presentation.websocket;

import com.pocketpick.chat.domain.message.MessageUseCase;
import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.WebSocketFrame;
import com.pocketpick.chat.domain.message.dto.WebSocketFrameType;
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

    @Mock private WebSocketSessionRegistry sessionRegistry;
    @Mock private OnlineStatusRepository onlineStatusRepository;
    @Mock private MessageUseCase messageUseCase;
    @Mock private ObjectMapper objectMapper;
    @Mock private WebSocketSession session;

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
            handler.afterConnectionEstablished(session);

            verify(sessionRegistry).register(42L, session);
            verify(onlineStatusRepository).markOnline(42L);
        }

        @Test
        @DisplayName("해제 시 세션 제거와 Redis 오프라인 표시를 한다")
        void afterConnectionClosed_removesSessionAndMarksOffline() {
            handler.afterConnectionClosed(session, CloseStatus.NORMAL);

            verify(sessionRegistry).remove(42L);
            verify(onlineStatusRepository).markOffline(42L);
        }
    }

    @Nested
    @DisplayName("메시지 처리")
    class MessageHandling {

        @Test
        @DisplayName("MESSAGE 프레임이면 MessageService.send()로 위임한다")
        void handleTextMessage_messageFrame_delegatesToSend() throws Exception {
            String json = "{\"frameType\":\"MESSAGE\",\"roomId\":\"room-1\",\"receiverId\":2,\"content\":\"안녕\",\"type\":\"TEXT\"}";
            WebSocketFrame frame = new WebSocketFrame();

            given(objectMapper.readValue(eq(json), eq(WebSocketFrame.class))).willReturn(frame);

            handler.handleMessage(session, new TextMessage(json));

            verify(messageUseCase).send(42L, frame);
        }

        @Test
        @DisplayName("ENTER_ROOM 프레임이면 currentRoom 세팅 후 읽음 처리를 한다")
        void handleTextMessage_enterRoomFrame_setsCurrentRoomAndMarksAsRead() throws Exception {
            String json = "{\"frameType\":\"ENTER_ROOM\",\"roomId\":\"room-1\"}";
            WebSocketFrame frame = new WebSocketFrame();
            frame.setFrameType(WebSocketFrameType.ENTER_ROOM);
            frame.setRoomId("room-1");

            given(objectMapper.readValue(eq(json), eq(WebSocketFrame.class))).willReturn(frame);

            handler.handleMessage(session, new TextMessage(json));

            verify(sessionRegistry).setCurrentRoom(42L, "room-1");
            verify(messageUseCase).markAsRead("room-1", 42L);
        }
    }
}
