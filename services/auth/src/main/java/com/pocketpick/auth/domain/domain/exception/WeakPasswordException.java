package com.pocketpick.auth.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class WeakPasswordException extends AccountException {
    public WeakPasswordException() {
        super("WEAK_PASSWORD", "비밀번호는 8자 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
    }
}
