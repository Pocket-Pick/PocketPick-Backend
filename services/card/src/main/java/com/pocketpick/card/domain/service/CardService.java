package com.pocketpick.card.domain.service;

import com.pocketpick.card.domain.dto.CardSearchRequest;
import com.pocketpick.card.domain.dto.CardSummaryResponse;
import com.pocketpick.card.domain.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService implements CardUseCase {

    private final CardRepository cardRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CardSummaryResponse> searchCards(CardSearchRequest request, Pageable pageable) {
        return cardRepository.search(request, pageable)
                .map(CardSummaryResponse::from);
    }
}
