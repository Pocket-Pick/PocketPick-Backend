package com.pocketpick.card.domain.domain.exception;

import com.pocketpick.card.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardErrorCode implements ErrorCode {

    CARD_NOT_FOUND("CARD_NOT_FOUND", "존재하지 않는 카드입니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
