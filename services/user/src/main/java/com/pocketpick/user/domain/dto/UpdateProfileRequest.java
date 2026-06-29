package com.pocketpick.user.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String nickname,
        @Nullable String profileImageUrl,
        @Nullable String region
) {
}
