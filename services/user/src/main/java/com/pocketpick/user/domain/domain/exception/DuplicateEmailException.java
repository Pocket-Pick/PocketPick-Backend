package com.pocketpick.user.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends UserException {
    public DuplicateEmailException() {
        super("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);
    }
}
