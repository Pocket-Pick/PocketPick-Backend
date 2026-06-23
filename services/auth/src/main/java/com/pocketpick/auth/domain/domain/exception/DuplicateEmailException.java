package com.pocketpick.auth.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends AccountException {
    public DuplicateEmailException() {
        super("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);
    }
}
