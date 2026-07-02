package com.pocketpick.card.domain.domain.exception;

public class CardNotFoundException extends CardException {

    public CardNotFoundException() {
        super(CardErrorCode.CARD_NOT_FOUND);
    }
}
