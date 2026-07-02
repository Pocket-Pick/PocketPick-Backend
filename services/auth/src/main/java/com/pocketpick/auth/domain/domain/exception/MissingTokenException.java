package com.pocketpick.auth.domain.domain.exception;

public class MissingTokenException extends AccountException {

    public MissingTokenException() {
        super(AuthErrorCode.MISSING_TOKEN);
    }
}
