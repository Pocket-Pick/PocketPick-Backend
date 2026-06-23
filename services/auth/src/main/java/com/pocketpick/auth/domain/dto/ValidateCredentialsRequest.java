package com.pocketpick.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ValidateCredentialsRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
