package com.pocketpick.card.domain.domain.exception;

import com.pocketpick.card.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public abstract class CardException extends BusinessException {
    protected CardException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
