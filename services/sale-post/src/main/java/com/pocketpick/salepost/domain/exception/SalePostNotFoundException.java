package com.pocketpick.salepost.domain.exception;

import com.pocketpick.salepost.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SalePostNotFoundException extends BusinessException {

    public SalePostNotFoundException() {
        super("SALE_POST_NOT_FOUND", "판매글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
