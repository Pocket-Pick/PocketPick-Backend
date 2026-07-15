package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.ChatMessageRepository;
import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.SendMessageRequest;
import com.pocketpick.chat.domain.outbox.OutboxEvent;
import com.pocketpick.chat.domain.outbox.OutboxEventRepository;
import com.pocketpick.chat.domain.room.ChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("MessageService")
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock private ChatMessageRepository chatMessageRepository;
    @Mock private ChatRoomRepository chatRoomRepository;
    @Mock private OutboxEventRepository outboxEventRepository;

    @InjectMocks
    private MessageService messageService;

    @Nested
    @DisplayName("메시지 전송")
    class Send {

        @Test
        @DisplayName("메시지를 MongoDB에 저장하고 Outbox에 PENDING으로 저장한다")
        void send_savesMessageAndOutboxEvent() throws Exception {
            ChatMessage saved = ChatMessage.builder()
                    .roomId("room-1").senderId(1L)
                    .content("안녕하세요").type(MessageType.TEXT).build();

            given(chatMessageRepository.save(any())).willReturn(saved);

            messageService.send(1L, buildRequest());

            verify(chatMessageRepository).save(any());
            verify(outboxEventRepository).save(any(OutboxEvent.class));
        }
    }

    private SendMessageRequest buildRequest() throws Exception {
        SendMessageRequest request = new SendMessageRequest();
        setField(request, "roomId", "room-1");
        setField(request, "receiverId", 2L);
        setField(request, "content", "안녕하세요");
        setField(request, "type", MessageType.TEXT);
        return request;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
