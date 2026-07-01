package com.pocketpick.card.domain.service;

import com.pocketpick.card.domain.dto.CardDetailResponse;
import com.pocketpick.card.domain.dto.CardSearchRequest;
import com.pocketpick.card.domain.dto.CardSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardUseCase {
    Page<CardSummaryResponse> searchCards(CardSearchRequest request, Pageable pageable);
    CardDetailResponse getCard(Long cardId);
}
