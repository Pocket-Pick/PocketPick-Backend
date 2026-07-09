package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.ChatMessageRepository;
import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.SendMessageRequest;
import com.pocketpick.chat.domain.room.ChatRoom;
import com.pocketpick.chat.domain.room.ChatRoomRepository;
import com.pocketpick.chat.infrastructure.kafka.ChatMessageProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("MessageService")
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageProducer chatMessageProducer;

    @InjectMocks
    private MessageService messageService;

    @Nested
    @DisplayName("메시지 전송")
    class Send {

        @Test
        @DisplayName("메시지를 MongoDB에 저장하고 Kafka로 발행한다")
        void send_validRequest_savesAndProducesToKafka() {
            // given
            Long senderId = 1L;
            SendMessageRequest request = new SendMessageRequest("room-1", 2L, "안녕하세요", MessageType.TEXT);

            ChatMessage saved = ChatMessage.builder()
                    .roomId("room-1")
                    .senderId(senderId)
                    .content("안녕하세요")
                    .type(MessageType.TEXT)
                    .build();

            given(chatMessageRepository.save(any())).willReturn(saved);
            given(chatRoomRepository.findById("room-1")).willReturn(Optional.empty());

            // when
            messageService.send(senderId, request);

            // then
            verify(chatMessageRepository).save(any());
            verify(chatMessageProducer).send(any());
        }

        @Test
        @DisplayName("채팅방이 존재하면 마지막 메시지를 갱신한다")
        void send_roomExists_updatesLastMessage() {
            // given
            Long senderId = 1L;
            SendMessageRequest request = new SendMessageRequest("room-1", 2L, "새 메시지", MessageType.TEXT);

            ChatMessage saved = ChatMessage.builder()
                    .roomId("room-1")
                    .senderId(senderId)
                    .content("새 메시지")
                    .type(MessageType.TEXT)
                    .build();

            ChatRoom room = ChatRoom.builder()
                    .buyerId(1L)
                    .sellerId(2L)
                    .salePostId(10L)
                    .build();

            given(chatMessageRepository.save(any())).willReturn(saved);
            given(chatRoomRepository.findById("room-1")).willReturn(Optional.of(room));

            // when
            messageService.send(senderId, request);

            // then
            ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);
            verify(chatRoomRepository).save(captor.capture());
            assertThat(captor.getValue().getLastMessage()).isEqualTo("새 메시지");
        }
    }
}
