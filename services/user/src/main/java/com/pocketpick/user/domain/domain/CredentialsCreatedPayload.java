package com.pocketpick.user.domain.domain;

public record CredentialsCreatedPayload(
        Long userId,
        String email,
        String encodedPassword
) {
}
