package com.pocketpick.user.domain.domain.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
