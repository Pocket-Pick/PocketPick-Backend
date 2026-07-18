package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.ChatMessageRepository;
import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.MessageHistoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@DisplayName("MessageHistoryService")
@ExtendWith(MockitoExtension.class)
class MessageHistoryServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private MessageHistoryService messageHistoryService;

    private ChatMessage buildMessage(String content) {
        return ChatMessage.builder()
                .roomId("room-1")
                .senderId(1L)
                .content(content)
                .type(MessageType.TEXT)
                .build();
    }

    @Nested
    @DisplayName("메시지 내역 조회")
    class GetHistory {

        @Test
        @DisplayName("cursor가 없으면 최신 메시지부터 조회한다")
        void getHistory_noCursor_returnsLatestMessages() {
            // given
            List<ChatMessage> messages = List.of(buildMessage("msg1"), buildMessage("msg2"));
            given(chatMessageRepository.findByRoomIdOrderByIdDesc(eq("room-1"), any(PageRequest.class)))
                    .willReturn(messages);

            // when
            MessageHistoryResponse response = messageHistoryService.getHistory("room-1", null, 20);

            // then
            assertThat(response.messages()).hasSize(2);
            assertThat(response.hasNext()).isFalse();
            assertThat(response.nextCursor()).isNull();
        }

        @Test
        @DisplayName("cursor가 있으면 해당 cursor 이전 메시지를 조회한다")
        void getHistory_withCursor_returnsMessagesBefore() {
            // given
            List<ChatMessage> messages = List.of(buildMessage("older msg"));
            given(chatMessageRepository.findByRoomIdAndIdLessThanOrderByIdDesc(eq("room-1"), eq("cursor-abc"), any(PageRequest.class)))
                    .willReturn(messages);

            // when
            MessageHistoryResponse response = messageHistoryService.getHistory("room-1", "cursor-abc", 20);

            // then
            assertThat(response.messages()).hasSize(1);
            assertThat(response.hasNext()).isFalse();
        }

        @Test
        @DisplayName("조회 결과가 size+1개이면 hasNext가 true이고 nextCursor를 반환한다")
        void getHistory_hasMorePages_returnsTrueHasNext() {
            // given
            List<ChatMessage> messages = List.of(
                    buildMessage("msg1"),
                    buildMessage("msg2"),
                    buildMessage("msg3")
            );
            given(chatMessageRepository.findByRoomIdOrderByIdDesc(eq("room-1"), any(PageRequest.class)))
                    .willReturn(messages);

            // when
            MessageHistoryResponse response = messageHistoryService.getHistory("room-1", null, 2);

            // then
            assertThat(response.messages()).hasSize(2);
            assertThat(response.hasNext()).isTrue();
        }
    }
}
