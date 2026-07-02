package com.pocketpick.auth.domain.domain.exception;

public class InvalidTokenException extends AccountException {

    public InvalidTokenException() {
        super(AuthErrorCode.INVALID_TOKEN);
    }
}
