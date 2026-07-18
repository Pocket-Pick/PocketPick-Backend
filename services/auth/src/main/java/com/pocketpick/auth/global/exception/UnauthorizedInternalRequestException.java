package com.pocketpick.auth.global.exception;

import com.pocketpick.auth.domain.domain.exception.AuthErrorCode;

public class UnauthorizedInternalRequestException extends BusinessException {

    public UnauthorizedInternalRequestException() {
        super(AuthErrorCode.UNAUTHORIZED_INTERNAL_REQUEST);
    }
}
