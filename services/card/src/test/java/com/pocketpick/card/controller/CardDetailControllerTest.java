package com.pocketpick.card.controller;

import com.pocketpick.card.domain.controller.CardController;
import com.pocketpick.card.domain.domain.exception.CardNotFoundException;
import com.pocketpick.card.domain.dto.CardDetailResponse;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CardController - 카드 상세 조회")
@ExtendWith(MockitoExtension.class)
class CardDetailControllerTest {

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
    @DisplayName("GET /cards/{id}")
    class GetCard {

        @Test
        @DisplayName("존재하는 id면 200을 반환한다")
        void getCard_existingId_returns200() throws Exception {
            // given
            CardDetailResponse response = CardDetailResponse.from(CardFixture.card());
            given(cardUseCase.getCard(CardFixture.ID)).willReturn(response);

            // when & then
            mockMvc.perform(get("/cards/{id}", CardFixture.ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(CardFixture.ID))
                    .andExpect(jsonPath("$.name").value(CardFixture.NAME))
                    .andExpect(jsonPath("$.setId").value(CardFixture.SET_ID));
        }

        @Test
        @DisplayName("존재하지 않는 id면 404를 반환한다")
        void getCard_notExistingId_returns404() throws Exception {
            // given
            given(cardUseCase.getCard(CardFixture.ID)).willThrow(new CardNotFoundException());

            // when & then
            mockMvc.perform(get("/cards/{id}", CardFixture.ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("CARD_NOT_FOUND"));
        }
    }
}
