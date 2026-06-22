package com.pocketpick.user.domain.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidNicknameException extends UserException {
    public InvalidNicknameException() {
        super("INVALID_NICKNAME", "닉네임은 2자 이상 20자 이하여야 합니다.", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
