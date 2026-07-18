package com.pocketpick.auth.domain.domain.exception;

import com.pocketpick.auth.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("INVALID_PASSWORD", "이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    WEAK_PASSWORD("WEAK_PASSWORD", "비밀번호는 8자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    MISSING_TOKEN("MISSING_TOKEN", "토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_INTERNAL_REQUEST("UNAUTHORIZED_INTERNAL_REQUEST", "내부 API 인증에 실패했습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
