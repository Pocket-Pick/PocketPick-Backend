package com.pocketpick.card.domain.service;

import com.pocketpick.card.domain.domain.Card;
import com.pocketpick.card.domain.domain.CardType;
import com.pocketpick.card.domain.domain.PokemonType;
import com.pocketpick.card.domain.domain.exception.CardNotFoundException;
import com.pocketpick.card.domain.dto.CardDetailResponse;
import com.pocketpick.card.domain.dto.CardSearchRequest;
import com.pocketpick.card.domain.dto.CardSummaryResponse;
import com.pocketpick.card.domain.repository.CardRepository;
import com.pocketpick.card.domain.repository.CardTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService implements CardUseCase {

    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CardSummaryResponse> searchCards(CardSearchRequest request, Pageable pageable) {
        Page<Card> cards = cardRepository.search(request, pageable);

        List<Long> cardIds = cards.map(card -> card.getId()).toList();
        Map<Long, List<PokemonType>> typesByCardId = cardTypeRepository.findByCardIdIn(cardIds).stream()
                .collect(Collectors.groupingBy(
                        CardType::getCardId,
                        Collectors.mapping(CardType::getType, Collectors.toList())
                ));

        return cards.map(card -> CardSummaryResponse.from(card, typesByCardId.getOrDefault(card.getId(), List.of())));
    }

    @Override
    @Transactional(readOnly = true)
    public CardDetailResponse getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .map(card -> {
                    List<PokemonType> types = cardTypeRepository.findByCardId(cardId).stream()
                            .map(ct -> ct.getType())
                            .toList();
                    return CardDetailResponse.from(card, types);
                })
                .orElseThrow(CardNotFoundException::new);
    }
}
