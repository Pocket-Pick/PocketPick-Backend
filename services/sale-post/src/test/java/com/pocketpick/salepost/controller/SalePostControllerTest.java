package com.pocketpick.salepost.controller;

import com.pocketpick.salepost.domain.dto.CreateSalePostRequest;
import com.pocketpick.salepost.domain.dto.SalePostResponse;
import com.pocketpick.salepost.domain.service.SalePostUseCase;
import com.pocketpick.salepost.domain.domain.CardCondition;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import com.pocketpick.salepost.domain.domain.exception.ForbiddenException;
import com.pocketpick.salepost.domain.domain.exception.SalePostNotFoundException;
import com.pocketpick.salepost.global.exception.GlobalExceptionHandler;
import com.pocketpick.salepost.domain.controller.SalePostController;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SalePostController")
@ExtendWith(MockitoExtension.class)
class SalePostControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private SalePostUseCase salePostUseCase;

    @InjectMocks
    private SalePostController salePostController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(salePostController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private SalePostResponse sampleResponse() {
        return new SalePostResponse(
                1L, 1L, 1L, "카드 팝니다", "상태 좋아요",
                10000, CardCondition.MINT, SaleStatus.ON_SALE,
                "https://bucket.s3.amazonaws.com/images/1/uuid.jpg",
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("POST /sale-posts")
    class CreateSalePost {

        @Test
        @DisplayName("정상 요청이면 201을 반환한다")
        void create_validRequest_returns201() throws Exception {
            // given
            CreateSalePostRequest request = new CreateSalePostRequest(
                    1L, "카드 팝니다", "상태 좋아요", 10000, CardCondition.MINT, "images/1/uuid.jpg"
            );
            given(salePostUseCase.create(eq(1L), any())).willReturn(sampleResponse());

            // when & then
            mockMvc.perform(post("/sale-posts")
                            .header("X-User-Id", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("카드 팝니다"))
                    .andExpect(jsonPath("$.price").value(10000));
        }

        @Test
        @DisplayName("title이 없으면 400을 반환한다")
        void create_missingTitle_returns400() throws Exception {
            // given
            CreateSalePostRequest request = new CreateSalePostRequest(
                    1L, "", "설명", 10000, CardCondition.MINT, null
            );

            // when & then
            mockMvc.perform(post("/sale-posts")
                            .header("X-User-Id", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /sale-posts/{id}")
    class GetOne {

        @Test
        @DisplayName("존재하는 ID면 200을 반환한다")
        void getOne_exists_returns200() throws Exception {
            // given
            given(salePostUseCase.getOne(1L)).willReturn(sampleResponse());

            // when & then
            mockMvc.perform(get("/sale-posts/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("존재하지 않는 ID면 404를 반환한다")
        void getOne_notFound_returns404() throws Exception {
            // given
            given(salePostUseCase.getOne(999L)).willThrow(new SalePostNotFoundException());

            // when & then
            mockMvc.perform(get("/sale-posts/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /sale-posts/{id}")
    class DeleteSalePost {

        @Test
        @DisplayName("본인이면 204를 반환한다")
        void delete_owner_returns204() throws Exception {
            // when & then
            mockMvc.perform(delete("/sale-posts/1")
                            .header("X-User-Id", "1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("본인이 아니면 403을 반환한다")
        void delete_notOwner_returns403() throws Exception {
            // given
            willThrow(new ForbiddenException()).given(salePostUseCase).delete(2L, 1L);

            // when & then
            mockMvc.perform(delete("/sale-posts/1")
                            .header("X-User-Id", "2"))
                    .andExpect(status().isForbidden());
        }
    }
}
