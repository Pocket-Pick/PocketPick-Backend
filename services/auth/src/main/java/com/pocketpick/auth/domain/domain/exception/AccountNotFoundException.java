package com.pocketpick.auth.domain.domain.exception;

public class AccountNotFoundException extends AccountException {

    public AccountNotFoundException() {
        super(AuthErrorCode.ACCOUNT_NOT_FOUND);
    }
}
