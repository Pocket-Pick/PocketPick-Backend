package com.pocketpick.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCredentialsRequest(
        @NotNull Long userId,
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
