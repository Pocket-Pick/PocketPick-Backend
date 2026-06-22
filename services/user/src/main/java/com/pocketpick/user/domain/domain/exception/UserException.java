package com.pocketpick.user.domain.domain.exception;

import com.pocketpick.user.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserException extends BusinessException {
    protected UserException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
