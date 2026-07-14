package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.ChatMessageRepository;
import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.WebSocketFrame;
import com.pocketpick.chat.domain.message.dto.WebSocketFrameType;
import com.pocketpick.chat.domain.room.ChatRoom;
import com.pocketpick.chat.domain.room.ChatRoomRepository;
import com.pocketpick.chat.infrastructure.kafka.ChatMessageProducer;
import com.pocketpick.chat.infrastructure.websocket.WebSocketMessageSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("MessageService")
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock private ChatMessageRepository chatMessageRepository;
    @Mock private ChatRoomRepository chatRoomRepository;
    @Mock private ChatMessageProducer chatMessageProducer;
    @Mock private WebSocketMessageSender webSocketMessageSender;
    @Mock private MongoTemplate mongoTemplate;

    @InjectMocks
    private MessageService messageService;

    @Nested
    @DisplayName("메시지 전송")
    class Send {

        @Test
        @DisplayName("메시지를 MongoDB에 저장하고 Kafka로 발행한다")
        void send_validRequest_savesAndProducesToKafka() {
            Long senderId = 1L;
            WebSocketFrame frame = buildMessageFrame("room-1", 2L, "안녕하세요");

            ChatMessage saved = ChatMessage.builder()
                    .roomId("room-1").senderId(senderId)
                    .content("안녕하세요").type(MessageType.TEXT).build();

            given(chatMessageRepository.save(any())).willReturn(saved);
            given(chatRoomRepository.findById("room-1")).willReturn(Optional.empty());

            messageService.send(senderId, frame);

            verify(chatMessageRepository).save(any());
            verify(chatMessageProducer).send(any());
        }

        @Test
        @DisplayName("채팅방이 존재하면 마지막 메시지를 갱신한다")
        void send_roomExists_updatesLastMessage() {
            Long senderId = 1L;
            WebSocketFrame frame = buildMessageFrame("room-1", 2L, "새 메시지");

            ChatMessage saved = ChatMessage.builder()
                    .roomId("room-1").senderId(senderId)
                    .content("새 메시지").type(MessageType.TEXT).build();

            ChatRoom room = ChatRoom.builder().buyerId(1L).sellerId(2L).salePostId(10L).build();

            given(chatMessageRepository.save(any())).willReturn(saved);
            given(chatRoomRepository.findById("room-1")).willReturn(Optional.of(room));

            messageService.send(senderId, frame);

            ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);
            verify(chatRoomRepository).save(captor.capture());
            assertThat(captor.getValue().getLastMessage()).isEqualTo("새 메시지");
        }
    }

    @Nested
    @DisplayName("읽음 처리")
    class MarkAsRead {

        @Test
        @DisplayName("미읽음 메시지가 있으면 벌크 업데이트 후 발신자에게 READ 이벤트를 전송한다")
        void markAsRead_unreadExists_updatesAndNotifiesSender() {
            ChatMessage unread = ChatMessage.builder()
                    .roomId("room-1").senderId(2L)
                    .content("안녕").type(MessageType.TEXT).build();

            given(chatMessageRepository.findTop1ByRoomIdAndSenderIdNotAndReadAtIsNull("room-1", 1L))
                    .willReturn(Optional.of(unread));

            messageService.markAsRead("room-1", 1L);

            verify(mongoTemplate).updateMulti(any(Query.class), any(Update.class), eq("messages"));
            verify(webSocketMessageSender).sendReadEvent(any(), any());
        }

        @Test
        @DisplayName("미읽음 메시지가 없으면 아무것도 하지 않는다")
        void markAsRead_noUnread_doesNothing() {
            given(chatMessageRepository.findTop1ByRoomIdAndSenderIdNotAndReadAtIsNull("room-1", 1L))
                    .willReturn(Optional.empty());

            messageService.markAsRead("room-1", 1L);

            verify(mongoTemplate, never()).updateMulti(any(), any(), anyString());
            verify(webSocketMessageSender, never()).sendReadEvent(any(), any());
        }
    }

    private WebSocketFrame buildMessageFrame(String roomId, Long receiverId, String content) {
        WebSocketFrame frame = new WebSocketFrame();
        frame.setFrameType(WebSocketFrameType.MESSAGE);
        frame.setRoomId(roomId);
        frame.setReceiverId(receiverId);
        frame.setContent(content);
        frame.setType(MessageType.TEXT);
        return frame;
    }
}
