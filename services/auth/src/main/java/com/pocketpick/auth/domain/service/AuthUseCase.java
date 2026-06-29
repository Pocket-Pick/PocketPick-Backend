package com.pocketpick.auth.domain.service;

import com.pocketpick.auth.domain.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthUseCase {
    void login(LoginRequest request, HttpServletResponse response);
}
