package com.pocketpick.auth.domain.domain.exception;

import com.pocketpick.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AccountException extends BusinessException {
    protected AccountException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
