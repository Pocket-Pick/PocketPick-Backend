package com.pocketpick.auth.domain.controller;

import com.pocketpick.auth.domain.dto.LoginRequest;
import com.pocketpick.auth.domain.service.AuthUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = Arrays.stream(request.getCookies())
                .filter(c -> "accessToken".equals(c.getName()))
                .findFirst()
                .map(c -> c.getValue())
                .orElseThrow(() -> new IllegalArgumentException("accessToken 쿠키가 없습니다."));

        authUseCase.logout(accessToken, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(c -> c.getValue())
                .orElseThrow(() -> new IllegalArgumentException("refreshToken 쿠키가 없습니다."));

        authUseCase.reissue(refreshToken, response);
        return ResponseEntity.ok().build();
    }

}
