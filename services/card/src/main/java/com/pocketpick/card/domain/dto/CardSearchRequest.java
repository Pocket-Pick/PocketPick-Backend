package com.pocketpick.card.domain.dto;

import com.pocketpick.card.domain.domain.PokemonType;
import com.pocketpick.card.domain.domain.Rarity;
import com.pocketpick.card.domain.domain.Supertype;

public record CardSearchRequest(
        String name,
        PokemonType type,
        Rarity rarity,
        String setId,
        Supertype supertype
) {
}
