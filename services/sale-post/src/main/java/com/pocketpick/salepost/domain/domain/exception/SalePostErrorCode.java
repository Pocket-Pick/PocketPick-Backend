package com.pocketpick.salepost.domain.domain.exception;

import com.pocketpick.salepost.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SalePostErrorCode implements ErrorCode {

    SALE_POST_NOT_FOUND("SALE_POST_NOT_FOUND", "판매글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("FORBIDDEN", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_STATUS_TRANSITION("INVALID_STATUS_TRANSITION", "유효하지 않은 상태 전이입니다.", HttpStatus.BAD_REQUEST),
    RESERVED_POST_CANNOT_BE_MODIFIED("RESERVED_POST_CANNOT_BE_MODIFIED", "예약 중인 판매글은 수정/삭제할 수 없습니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
