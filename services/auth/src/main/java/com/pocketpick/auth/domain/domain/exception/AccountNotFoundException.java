package com.pocketpick.auth.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException() {
        super("ACCOUNT_NOT_FOUND", "이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
