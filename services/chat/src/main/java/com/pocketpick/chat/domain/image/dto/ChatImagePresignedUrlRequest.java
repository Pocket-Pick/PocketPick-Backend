package com.pocketpick.chat.domain.image.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChatImagePresignedUrlRequest(

        @NotBlank(message = "파일 확장자는 필수입니다.")
        @Pattern(regexp = "^(jpg|jpeg|png|gif|webp)$", message = "지원하지 않는 파일 형식입니다.")
        String extension
) {
}
