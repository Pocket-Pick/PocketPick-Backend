package com.pocketpick.auth.domain.domain.exception;

public class DuplicateEmailException extends AccountException {

    public DuplicateEmailException() {
        super(AuthErrorCode.DUPLICATE_EMAIL);
    }
}
