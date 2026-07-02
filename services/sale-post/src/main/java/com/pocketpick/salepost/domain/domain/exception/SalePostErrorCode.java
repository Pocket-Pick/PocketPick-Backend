package com.pocketpick.salepost.domain.domain.exception;

import com.pocketpick.salepost.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SalePostErrorCode implements ErrorCode {

    SALE_POST_NOT_FOUND("SALE_POST_NOT_FOUND", "판매글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("FORBIDDEN", "권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
