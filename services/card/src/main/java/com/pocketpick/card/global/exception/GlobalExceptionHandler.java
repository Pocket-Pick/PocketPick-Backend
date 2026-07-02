package com.pocketpick.card.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(CommonErrorCode.INVALID_INPUT.getHttpStatus())
                .body(ErrorResponse.of(CommonErrorCode.INVALID_INPUT.getCode(),
                        "올바르지 않은 파라미터 값입니다: " + e.getName()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.status(CommonErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
