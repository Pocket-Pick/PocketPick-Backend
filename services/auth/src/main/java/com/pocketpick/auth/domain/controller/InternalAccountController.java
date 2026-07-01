package com.pocketpick.auth.domain.controller;

import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;
import com.pocketpick.auth.domain.dto.ValidateCredentialsRequest;
import com.pocketpick.auth.domain.service.AccountUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/accounts")
@RequiredArgsConstructor
public class InternalAccountController {

    private final AccountUseCase accountUseCase;

    @PostMapping("/validate")
    public ResponseEntity<Void> validate(@Valid @RequestBody ValidateCredentialsRequest request) {
        accountUseCase.validate(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credentials")
    public ResponseEntity<Void> createCredentials(@Valid @RequestBody CreateCredentialsRequest request) {
        accountUseCase.createCredentials(request);
        return ResponseEntity.ok().build();
    }
}
