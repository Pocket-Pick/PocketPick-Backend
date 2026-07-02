package com.pocketpick.salepost.domain.exception;

import com.pocketpick.salepost.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {

    public ForbiddenException() {
        super("FORBIDDEN", "권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
