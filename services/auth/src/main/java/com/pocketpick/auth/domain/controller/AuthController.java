package com.pocketpick.auth.domain.controller;

import com.pocketpick.auth.domain.dto.LoginRequest;
import com.pocketpick.auth.domain.service.AuthUseCase;
import com.pocketpick.auth.infrastructure.cookie.CookieProvider;
import jakarta.servlet.http.HttpServletRequest;
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
    private final CookieProvider cookieProvider;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authUseCase.login(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = cookieProvider.extractCookie(request, "accessToken");
        authUseCase.logout(accessToken, response);
        return ResponseEntity.ok().build();
    }
}
