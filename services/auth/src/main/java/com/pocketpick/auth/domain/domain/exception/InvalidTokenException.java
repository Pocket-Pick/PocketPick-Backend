package com.pocketpick.auth.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AccountException {
    public InvalidTokenException() {
        super("INVALID_TOKEN", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
    }
}
