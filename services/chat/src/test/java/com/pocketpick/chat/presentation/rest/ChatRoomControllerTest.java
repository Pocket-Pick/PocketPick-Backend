package com.pocketpick.chat.presentation.rest;

import com.pocketpick.chat.domain.room.ChatRoomUseCase;
import com.pocketpick.chat.domain.room.dto.ChatRoomResponse;
import com.pocketpick.chat.domain.room.dto.CreateChatRoomRequest;
import com.pocketpick.chat.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ChatRoomController")
@ExtendWith(MockitoExtension.class)
class ChatRoomControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ChatRoomUseCase chatRoomUseCase;

    @InjectMocks
    private ChatRoomController chatRoomController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatRoomController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /chat/rooms")
    class CreateOrGet {

        @Test
        @DisplayName("정상 요청이면 200을 반환한다")
        void createOrGet_validRequest_returns200() throws Exception {
            // given
            CreateChatRoomRequest request = new CreateChatRoomRequest(1L, 2L, 10L);
            ChatRoomResponse response = new ChatRoomResponse("room-1", 1L, 2L, 10L, null, LocalDateTime.now());
            given(chatRoomUseCase.createOrGet(any())).willReturn(response);

            // when & then
            mockMvc.perform(post("/chat/rooms")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roomId").value("room-1"))
                    .andExpect(jsonPath("$.buyerId").value(1))
                    .andExpect(jsonPath("$.sellerId").value(2));
        }

        @Test
        @DisplayName("buyerId가 null이면 400을 반환한다")
        void createOrGet_nullBuyerId_returns400() throws Exception {
            // given
            String invalidBody = "{\"buyerId\": null, \"sellerId\": 2, \"salePostId\": 10}";

            // when & then
            mockMvc.perform(post("/chat/rooms")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
        }
    }

    @Nested
    @DisplayName("GET /chat/rooms")
    class ListRooms {

        @Test
        @DisplayName("userId로 채팅방 목록을 반환한다")
        void listRooms_validUserId_returns200() throws Exception {
            // given
            ChatRoomResponse room = new ChatRoomResponse("room-1", 1L, 2L, 10L, "마지막 메시지", LocalDateTime.now());
            given(chatRoomUseCase.listRooms(eq(1L))).willReturn(List.of(room));

            // when & then
            mockMvc.perform(get("/chat/rooms").param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].roomId").value("room-1"))
                    .andExpect(jsonPath("$[0].lastMessage").value("마지막 메시지"));
        }

        @Test
        @DisplayName("참여한 채팅방이 없으면 빈 배열을 반환한다")
        void listRooms_noRooms_returnsEmptyArray() throws Exception {
            // given
            given(chatRoomUseCase.listRooms(eq(99L))).willReturn(List.of());

            // when & then
            mockMvc.perform(get("/chat/rooms").param("userId", "99"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }
}
