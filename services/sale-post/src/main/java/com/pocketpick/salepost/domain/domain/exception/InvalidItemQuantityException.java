package com.pocketpick.salepost.domain.domain.exception;

import com.pocketpick.salepost.global.exception.BusinessException;

public class InvalidItemQuantityException extends BusinessException {

    public InvalidItemQuantityException() {
        super(SalePostErrorCode.INVALID_ITEM_QUANTITY);
    }
}
