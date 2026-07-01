package com.pocketpick.card.service;

import com.pocketpick.card.domain.domain.CardType;
import com.pocketpick.card.domain.domain.PokemonType;
import com.pocketpick.card.domain.domain.exception.CardNotFoundException;
import com.pocketpick.card.domain.dto.CardDetailResponse;
import com.pocketpick.card.domain.dto.CardSearchRequest;
import com.pocketpick.card.domain.dto.CardSummaryResponse;
import com.pocketpick.card.domain.repository.CardRepository;
import com.pocketpick.card.domain.repository.CardTypeRepository;
import com.pocketpick.card.domain.service.CardService;
import com.pocketpick.card.support.fixture.CardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("CardService")
@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardTypeRepository cardTypeRepository;

    @InjectMocks
    private CardService cardService;

    @Nested
    @DisplayName("카드 검색")
    class SearchCards {

        @Test
        @DisplayName("조건 없이 검색하면 전체 카드를 페이지네이션해서 반환한다")
        void searchCards_noCondition_returnsAllCards() {
            // given
            CardSearchRequest request = new CardSearchRequest(null, null, null, null, null);
            Pageable pageable = PageRequest.of(0, 20);
            given(cardRepository.search(request, pageable))
                    .willReturn(new PageImpl<>(List.of(CardFixture.card()), pageable, 1));

            // when
            Page<CardSummaryResponse> result = cardService.searchCards(request, pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).name()).isEqualTo(CardFixture.NAME);
        }

        @Test
        @DisplayName("카드명으로 검색하면 일치하는 카드를 반환한다")
        void searchCards_byName_returnsMatchingCards() {
            // given
            CardSearchRequest request = new CardSearchRequest("Charizard", null, null, null, null);
            Pageable pageable = PageRequest.of(0, 20);
            given(cardRepository.search(request, pageable))
                    .willReturn(new PageImpl<>(List.of(CardFixture.card()), pageable, 1));

            // when
            Page<CardSummaryResponse> result = cardService.searchCards(request, pageable);

            // then
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).name()).isEqualTo("Charizard");
        }

        @Test
        @DisplayName("조건에 맞는 카드가 없으면 빈 페이지를 반환한다")
        void searchCards_noMatch_returnsEmptyPage() {
            // given
            CardSearchRequest request = new CardSearchRequest("없는카드", null, null, null, null);
            Pageable pageable = PageRequest.of(0, 20);
            given(cardRepository.search(request, pageable))
                    .willReturn(new PageImpl<>(List.of(), pageable, 0));

            // when
            Page<CardSummaryResponse> result = cardService.searchCards(request, pageable);

            // then
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("속성(type)으로 검색하면 해당 속성을 가진 카드를 반환한다")
        void searchCards_byType_returnsMatchingCards() {
            // given
            CardSearchRequest request = new CardSearchRequest(null, PokemonType.FIRE, null, null, null);
            Pageable pageable = PageRequest.of(0, 20);
            given(cardRepository.search(request, pageable))
                    .willReturn(new PageImpl<>(List.of(CardFixture.card()), pageable, 1));

            // when
            Page<CardSummaryResponse> result = cardService.searchCards(request, pageable);

            // then
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).name()).isEqualTo(CardFixture.NAME);
        }
    }

    @Nested
    @DisplayName("카드 상세 조회")
    class GetCard {

        @Test
        @DisplayName("존재하는 id면 카드 상세 응답을 반환한다")
        void getCard_existingId_returnsCardDetailResponse() {
            // given
            CardType cardType = new CardType();
            ReflectionTestUtils.setField(cardType, "id", 1L);
            ReflectionTestUtils.setField(cardType, "cardId", CardFixture.ID);
            ReflectionTestUtils.setField(cardType, "type", PokemonType.FIRE);

            given(cardRepository.findById(CardFixture.ID)).willReturn(Optional.of(CardFixture.card()));
            given(cardTypeRepository.findByCardId(CardFixture.ID)).willReturn(List.of(cardType));

            // when
            CardDetailResponse response = cardService.getCard(CardFixture.ID);

            // then
            assertThat(response.id()).isEqualTo(CardFixture.ID);
            assertThat(response.name()).isEqualTo(CardFixture.NAME);
            assertThat(response.setId()).isEqualTo(CardFixture.SET_ID);
            assertThat(response.types()).containsExactly(PokemonType.FIRE);
        }

        @Test
        @DisplayName("존재하지 않는 id면 CardNotFoundException을 던진다")
        void getCard_notExistingId_throwsCardNotFoundException() {
            // given
            given(cardRepository.findById(CardFixture.ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> cardService.getCard(CardFixture.ID))
                    .isInstanceOf(CardNotFoundException.class);
        }
    }
}
