package com.pocketpick.card.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class CardNotFoundException extends CardException {
    public CardNotFoundException() {
        super("CARD_NOT_FOUND", "존재하지 않는 카드입니다.", HttpStatus.NOT_FOUND);
    }
}
