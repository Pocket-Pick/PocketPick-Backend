package com.pocketpick.user.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends UserException {
    public InvalidPasswordException() {
        super("INVALID_PASSWORD", "비밀번호는 8자 이상이어야 합니다.", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
