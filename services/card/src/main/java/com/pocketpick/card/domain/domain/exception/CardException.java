package com.pocketpick.card.domain.domain.exception;

import com.pocketpick.card.global.exception.BusinessException;
import com.pocketpick.card.global.exception.ErrorCode;

public abstract class CardException extends BusinessException {

    protected CardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
