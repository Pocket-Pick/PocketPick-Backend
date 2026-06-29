package com.pocketpick.user.infrastructure.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {

    private final RestClient restClient;

    public void validate(String email, String password) {
        restClient.post()
                .uri("/internal/accounts/validate")
                .body(Map.of("email", email, "password", password))
                .retrieve()
                .toBodilessEntity();
    }

    public void createCredentials(Long userId, String email, String encodedPassword) {
        restClient.post()
                .uri("/internal/accounts/credentials")
                .body(Map.of("userId", userId, "email", email, "encodedPassword", encodedPassword))
                .retrieve()
                .toBodilessEntity();
    }
}
