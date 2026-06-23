package com.pocketpick.card.domain.dto;

import com.pocketpick.card.domain.domain.Card;
import com.pocketpick.card.domain.domain.PokemonType;
import com.pocketpick.card.domain.domain.Rarity;
import com.pocketpick.card.domain.domain.Supertype;

import java.util.List;

public record CardSummaryResponse(
        Long id,
        String name,
        Supertype supertype,
        String subtype,
        Rarity rarity,
        String setId,
        String setName,
        String number,
        String imageSmallUrl,
        List<PokemonType> types
) {
    public static CardSummaryResponse from(Card card) {
        return new CardSummaryResponse(
                card.getId(),
                card.getName(),
                card.getSupertype(),
                card.getSubtype(),
                card.getRarity(),
                card.getSet().getId(),
                card.getSet().getName(),
                card.getNumber(),
                card.getImageSmallUrl(),
                card.getTypes().stream().map(t -> t.getType()).toList()
        );
    }
}
