package com.pocketpick.salepost.domain.domain.exception;

import com.pocketpick.salepost.global.exception.BusinessException;

public class InvalidStatusTransitionException extends BusinessException {

    public InvalidStatusTransitionException() {
        super(SalePostErrorCode.INVALID_STATUS_TRANSITION);
    }
}
