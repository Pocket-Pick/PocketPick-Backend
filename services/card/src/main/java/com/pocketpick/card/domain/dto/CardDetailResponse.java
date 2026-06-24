package com.pocketpick.card.domain.dto;

import com.pocketpick.card.domain.domain.Card;
import com.pocketpick.card.domain.domain.PokemonType;
import com.pocketpick.card.domain.domain.Rarity;
import com.pocketpick.card.domain.domain.Supertype;

import java.util.List;

public record CardDetailResponse(
        Long id,
        String name,
        Supertype supertype,
        String subtype,
        Rarity rarity,
        String setId,
        String number,
        String imageSmallUrl,
        String imageLargeUrl,
        List<PokemonType> types
) {
    public static CardDetailResponse from(Card card) {
        return new CardDetailResponse(
                card.getId(),
                card.getName(),
                card.getSupertype(),
                card.getSubtype(),
                card.getRarity(),
                card.getSetId(),
                card.getNumber(),
                card.getImageSmallUrl(),
                card.getImageLargeUrl(),
                card.getTypes().stream().map(t -> t.getType()).toList()
        );
    }
}
