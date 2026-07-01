package com.pocketpick.card.support.fixture;

import com.pocketpick.card.domain.domain.Card;
import com.pocketpick.card.domain.domain.Rarity;
import com.pocketpick.card.domain.domain.Supertype;
import org.springframework.test.util.ReflectionTestUtils;

public class CardFixture {

    public static final Long ID = 1L;
    public static final String NAME = "Charizard";
    public static final Supertype SUPERTYPE = Supertype.POKEMON;
    public static final String SUBTYPE = "Stage 2";
    public static final Rarity RARITY = Rarity.RARE_HOLO;
    public static final String SET_ID = "base1";
    public static final String NUMBER = "4";

    public static Card card() {
        Card card = new Card();
        ReflectionTestUtils.setField(card, "id", ID);
        ReflectionTestUtils.setField(card, "name", NAME);
        ReflectionTestUtils.setField(card, "supertype", SUPERTYPE);
        ReflectionTestUtils.setField(card, "subtype", SUBTYPE);
        ReflectionTestUtils.setField(card, "rarity", RARITY);
        ReflectionTestUtils.setField(card, "number", NUMBER);
        ReflectionTestUtils.setField(card, "setId", SET_ID);
        ReflectionTestUtils.setField(card, "imageSmallUrl", "https://images.pokemontcg.io/base1/4.png");
        ReflectionTestUtils.setField(card, "imageLargeUrl", "https://images.pokemontcg.io/base1/4_hires.png");
        return card;
    }
}
