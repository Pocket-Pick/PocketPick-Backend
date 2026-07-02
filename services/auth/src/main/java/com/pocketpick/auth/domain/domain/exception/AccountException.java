package com.pocketpick.auth.domain.domain.exception;

import com.pocketpick.auth.global.exception.BusinessException;
import com.pocketpick.auth.global.exception.ErrorCode;

public abstract class AccountException extends BusinessException {

    protected AccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
