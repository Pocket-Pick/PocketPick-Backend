package com.pocketpick.chat.presentation.rest;

import com.pocketpick.chat.domain.image.ChatImageUseCase;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlRequest;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ChatImageController")
@ExtendWith(MockitoExtension.class)
class ChatImageControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ChatImageUseCase chatImageUseCase;

    @InjectMocks
    private ChatImageController chatImageController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatImageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /chat/image/presigned-url")
    class GeneratePresignedUrl {

        @Test
        @DisplayName("유효한 요청이면 200과 presignedUrl, imageUrl을 반환한다")
        void generatePresignedUrl_validRequest_returns200() throws Exception {
            ChatImagePresignedUrlRequest request = new ChatImagePresignedUrlRequest("jpg");
            ChatImagePresignedUrlResponse response = new ChatImagePresignedUrlResponse(
                    "https://s3.amazonaws.com/bucket/images/chat/1/uuid.jpg?X-Amz-Signature=abc",
                    "https://bucket.s3.amazonaws.com/images/chat/1/uuid.jpg"
            );
            given(chatImageUseCase.generatePresignedUrl(eq(1L), any())).willReturn(response);

            mockMvc.perform(post("/chat/image/presigned-url")
                            .header("X-User-Id", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.presignedUrl").value(response.presignedUrl()))
                    .andExpect(jsonPath("$.imageUrl").value(response.imageUrl()));
        }

        @Test
        @DisplayName("extension이 빈 문자열이면 400을 반환한다")
        void generatePresignedUrl_blankExtension_returns400() throws Exception {
            String body = "{\"extension\": \"\"}";

            mockMvc.perform(post("/chat/image/presigned-url")
                            .header("X-User-Id", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
        }

        @Test
        @DisplayName("허용되지 않는 확장자이면 400을 반환한다")
        void generatePresignedUrl_invalidExtension_returns400() throws Exception {
            String body = "{\"extension\": \"exe\"}";

            mockMvc.perform(post("/chat/image/presigned-url")
                            .header("X-User-Id", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
        }
    }
}
