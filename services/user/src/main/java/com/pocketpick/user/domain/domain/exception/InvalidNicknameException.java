package com.pocketpick.user.domain.domain.exception;

public class InvalidNicknameException extends UserException {

    public InvalidNicknameException() {
        super(UserErrorCode.INVALID_NICKNAME);
    }
}
