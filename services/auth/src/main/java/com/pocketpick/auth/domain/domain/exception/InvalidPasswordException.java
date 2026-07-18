package com.pocketpick.auth.domain.domain.exception;

public class InvalidPasswordException extends AccountException {

    public InvalidPasswordException() {
        super(AuthErrorCode.INVALID_PASSWORD);
    }
}
