package com.pocketpick.auth.domain.controller;

import com.pocketpick.auth.domain.dto.LoginRequest;
import com.pocketpick.auth.domain.service.AuthUseCase;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authUseCase.login(request, response);
        return ResponseEntity.ok().build();
    }

}
