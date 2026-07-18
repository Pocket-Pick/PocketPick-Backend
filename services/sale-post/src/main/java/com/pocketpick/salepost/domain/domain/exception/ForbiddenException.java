package com.pocketpick.salepost.domain.domain.exception;

import com.pocketpick.salepost.global.exception.BusinessException;

public class ForbiddenException extends BusinessException {

    public ForbiddenException() {
        super(SalePostErrorCode.FORBIDDEN);
    }
}
