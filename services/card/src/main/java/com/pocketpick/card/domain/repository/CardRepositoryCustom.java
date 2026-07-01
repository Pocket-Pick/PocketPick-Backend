package com.pocketpick.card.domain.repository;

import com.pocketpick.card.domain.domain.Card;
import com.pocketpick.card.domain.dto.CardSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRepositoryCustom {
    Page<Card> search(CardSearchRequest request, Pageable pageable);
}
