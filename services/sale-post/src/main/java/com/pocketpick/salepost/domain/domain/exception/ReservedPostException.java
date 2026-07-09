package com.pocketpick.salepost.domain.domain.exception;

import com.pocketpick.salepost.global.exception.BusinessException;

public class ReservedPostException extends BusinessException {

    public ReservedPostException() {
        super(SalePostErrorCode.RESERVED_POST_CANNOT_BE_MODIFIED);
    }
}
