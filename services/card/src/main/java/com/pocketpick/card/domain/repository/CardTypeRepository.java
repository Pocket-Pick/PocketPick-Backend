package com.pocketpick.card.domain.repository;

import com.pocketpick.card.domain.domain.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {
    List<CardType> findByCardId(Long cardId);

    List<CardType> findByCardIdIn(List<Long> cardIds);
}
