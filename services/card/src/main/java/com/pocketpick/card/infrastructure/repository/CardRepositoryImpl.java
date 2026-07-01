package com.pocketpick.card.infrastructure.repository;

import com.pocketpick.card.domain.domain.Card;
import com.pocketpick.card.domain.domain.PokemonType;
import com.pocketpick.card.domain.domain.QCard;
import com.pocketpick.card.domain.domain.QCardType;
import com.pocketpick.card.domain.dto.CardSearchRequest;
import com.pocketpick.card.domain.repository.CardRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Card> search(CardSearchRequest request, Pageable pageable) {
        QCard card = QCard.card;

        List<Card> content = queryFactory
                .selectFrom(card)
                .where(
                        nameContains(request.name()),
                        supertypeEq(request.supertype()),
                        rarityEq(request.rarity()),
                        setIdEq(request.setId()),
                        typeEq(request.type())
                )
                .orderBy(card.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(card.count())
                .from(card)
                .where(
                        nameContains(request.name()),
                        supertypeEq(request.supertype()),
                        rarityEq(request.rarity()),
                        setIdEq(request.setId()),
                        typeEq(request.type())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression nameContains(String name) {
        return name != null ? QCard.card.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression supertypeEq(com.pocketpick.card.domain.domain.Supertype supertype) {
        return supertype != null ? QCard.card.supertype.eq(supertype) : null;
    }

    private BooleanExpression rarityEq(com.pocketpick.card.domain.domain.Rarity rarity) {
        return rarity != null ? QCard.card.rarity.eq(rarity) : null;
    }

    private BooleanExpression setIdEq(String setId) {
        return setId != null ? QCard.card.setId.eq(setId) : null;
    }

    private BooleanExpression typeEq(PokemonType type) {
        if (type == null) return null;
        QCardType cardType = QCardType.cardType;
        return QCard.card.id.in(
                JPAExpressions.select(cardType.cardId)
                        .from(cardType)
                        .where(cardType.type.eq(type))
        );
    }
}
