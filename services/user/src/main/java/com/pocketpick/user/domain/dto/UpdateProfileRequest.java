package com.pocketpick.user.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String nickname,
        String profileImageUrl,
        String region
) {
}
