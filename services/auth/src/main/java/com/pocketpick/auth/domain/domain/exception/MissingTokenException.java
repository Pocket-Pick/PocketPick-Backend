package com.pocketpick.auth.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class MissingTokenException extends AccountException {
    public MissingTokenException() {
        super("MISSING_TOKEN", "토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
