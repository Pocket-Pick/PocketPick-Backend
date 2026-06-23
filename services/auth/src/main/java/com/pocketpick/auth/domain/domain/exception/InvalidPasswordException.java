package com.pocketpick.auth.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends AccountException {
    public InvalidPasswordException() {
        super("INVALID_PASSWORD", "이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
