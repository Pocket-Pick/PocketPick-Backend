package com.pocketpick.user.domain.domain.exception;

import com.pocketpick.user.global.exception.BusinessException;
import com.pocketpick.user.global.exception.ErrorCode;

public class UserException extends BusinessException {

    protected UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
