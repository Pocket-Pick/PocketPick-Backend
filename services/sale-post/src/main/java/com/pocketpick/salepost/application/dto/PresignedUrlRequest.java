package com.pocketpick.salepost.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PresignedUrlRequest(
        @NotBlank
        @Pattern(regexp = "^[^.]+\\.(jpg|jpeg|png|webp)$",
                message = "jpg, jpeg, png, webp 파일만 업로드 가능합니다.")
        String filename
) {
}
