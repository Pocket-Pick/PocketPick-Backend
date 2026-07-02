package com.pocketpick.salepost.domain.domain.exception;

import com.pocketpick.salepost.global.exception.BusinessException;

public class SalePostNotFoundException extends BusinessException {

    public SalePostNotFoundException() {
        super(SalePostErrorCode.SALE_POST_NOT_FOUND);
    }
}
