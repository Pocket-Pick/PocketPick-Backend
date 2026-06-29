package com.pocketpick.auth.global.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedInternalRequestException extends BusinessException {
    public UnauthorizedInternalRequestException() {
        super("UNAUTHORIZED_INTERNAL_REQUEST", "내부 API 인증에 실패했습니다.", HttpStatus.FORBIDDEN);
    }
}
