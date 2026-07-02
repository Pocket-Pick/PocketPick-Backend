package com.pocketpick.auth.domain.domain.exception;

public class WeakPasswordException extends AccountException {

    public WeakPasswordException() {
        super(AuthErrorCode.WEAK_PASSWORD);
    }
}
