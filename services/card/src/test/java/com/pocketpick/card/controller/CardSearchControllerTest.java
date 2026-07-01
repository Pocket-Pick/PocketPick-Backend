package com.pocketpick.card.controller;

import com.pocketpick.card.domain.controller.CardController;
import com.pocketpick.card.domain.dto.CardSearchRequest;
import com.pocketpick.card.domain.dto.CardSummaryResponse;
import com.pocketpick.card.domain.service.CardUseCase;
import com.pocketpick.card.global.exception.GlobalExceptionHandler;
import com.pocketpick.card.support.fixture.CardFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CardController - 카드 검색")
@ExtendWith(MockitoExtension.class)
class CardSearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardUseCase cardUseCase;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Nested
    @DisplayName("GET /cards")
    class SearchCards {

        @Test
        @DisplayName("조건 없이 요청하면 200을 반환한다")
        void searchCards_noParams_returns200() throws Exception {
            // given
            CardSummaryResponse response = CardSummaryResponse.from(CardFixture.card());
            given(cardUseCase.searchCards(any(CardSearchRequest.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(response)));

            // when & then
            mockMvc.perform(get("/cards"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value(CardFixture.NAME))
                    .andExpect(jsonPath("$.content[0].supertype").value("POKEMON"));
        }

        @Test
        @DisplayName("카드명 파라미터로 요청하면 200을 반환한다")
        void searchCards_withName_returns200() throws Exception {
            // given
            CardSummaryResponse response = CardSummaryResponse.from(CardFixture.card());
            given(cardUseCase.searchCards(any(CardSearchRequest.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(response)));

            // when & then
            mockMvc.perform(get("/cards").param("name", "Charizard"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Charizard"));
        }

        @Test
        @DisplayName("잘못된 type 파라미터이면 400을 반환한다")
        void searchCards_invalidType_returns400() throws Exception {
            // when & then
            mockMvc.perform(get("/cards").param("type", "INVALID_TYPE"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT_VALUE"));
        }

        @Test
        @DisplayName("결과가 없으면 빈 page를 반환한다")
        void searchCards_noResult_returnsEmptyPage() throws Exception {
            // given
            given(cardUseCase.searchCards(any(CardSearchRequest.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of()));

            // when & then
            mockMvc.perform(get("/cards").param("name", "없는카드"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.totalElements").value(0));
        }
    }
}
