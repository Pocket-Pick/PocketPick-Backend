package com.pocketpick.card.domain.repository;

import com.pocketpick.card.domain.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
}
