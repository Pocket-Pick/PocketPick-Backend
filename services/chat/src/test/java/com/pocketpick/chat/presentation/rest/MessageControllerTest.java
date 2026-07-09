package com.pocketpick.chat.presentation.rest;

import com.pocketpick.chat.application.MessageHistoryService;
import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.MessageHistoryResponse;
import com.pocketpick.chat.domain.message.dto.MessageHistoryResponse.MessageItem;
import com.pocketpick.chat.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MessageController")
@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MessageHistoryService messageHistoryService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("GET /chat/rooms/{roomId}/messages")
    class GetMessages {

        @Test
        @DisplayName("cursor 없이 요청하면 최신 메시지를 반환한다")
        void getMessages_noCursor_returnsLatestMessages() throws Exception {
            // given
            MessageItem item = new MessageItem("msg-1", 1L, "안녕하세요", MessageType.TEXT, LocalDateTime.now());
            MessageHistoryResponse response = new MessageHistoryResponse(List.of(item), null, false);
            given(messageHistoryService.getHistory(eq("room-1"), isNull(), eq(20)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(get("/chat/rooms/room-1/messages"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.messages[0].messageId").value("msg-1"))
                    .andExpect(jsonPath("$.messages[0].content").value("안녕하세요"))
                    .andExpect(jsonPath("$.hasNext").value(false))
                    .andExpect(jsonPath("$.nextCursor").doesNotExist());
        }

        @Test
        @DisplayName("cursor와 함께 요청하면 해당 cursor 이전 메시지를 반환한다")
        void getMessages_withCursor_returnsOlderMessages() throws Exception {
            // given
            MessageItem item = new MessageItem("msg-0", 2L, "이전 메시지", MessageType.TEXT, LocalDateTime.now());
            MessageHistoryResponse response = new MessageHistoryResponse(List.of(item), null, false);
            given(messageHistoryService.getHistory(eq("room-1"), eq("cursor-abc"), eq(20)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(get("/chat/rooms/room-1/messages")
                            .param("cursor", "cursor-abc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.messages[0].content").value("이전 메시지"))
                    .andExpect(jsonPath("$.hasNext").value(false));
        }

        @Test
        @DisplayName("다음 페이지가 있으면 nextCursor를 반환한다")
        void getMessages_hasNextPage_returnsNextCursor() throws Exception {
            // given
            MessageItem item = new MessageItem("msg-5", 1L, "메시지", MessageType.TEXT, LocalDateTime.now());
            MessageHistoryResponse response = new MessageHistoryResponse(List.of(item), "msg-5", true);
            given(messageHistoryService.getHistory(eq("room-1"), isNull(), eq(1)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(get("/chat/rooms/room-1/messages").param("size", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.hasNext").value(true))
                    .andExpect(jsonPath("$.nextCursor").value("msg-5"));
        }
    }
}
