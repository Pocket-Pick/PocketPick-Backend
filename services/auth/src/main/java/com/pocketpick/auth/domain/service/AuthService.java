package com.pocketpick.auth.domain.service;

import com.pocketpick.auth.domain.domain.Account;
import com.pocketpick.auth.domain.domain.exception.AccountNotFoundException;
import com.pocketpick.auth.domain.dto.LoginRequest;
import com.pocketpick.auth.domain.repository.AccountRepository;
import com.pocketpick.auth.infrastructure.cookie.CookieProvider;
import com.pocketpick.auth.infrastructure.jwt.JwtProvider;
import com.pocketpick.auth.infrastructure.jwt.TokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenManager tokenManager;
    private final CookieProvider cookieProvider;

    @Override
    @Transactional
    public void login(LoginRequest request, HttpServletResponse response) {
        Account account = accountRepository.findByEmail(request.email())
                .orElseThrow(AccountNotFoundException::new);

        account.checkPassword(request.password(), passwordEncoder);

        String[] tokens = tokenManager.createTokens(account.getUserId());
        cookieProvider.addTokenCookies(response, tokens[0], tokens[1]);
    }

    @Override
    public void logout(String accessToken, HttpServletResponse response) {
        try {
            Long userId = jwtProvider.getUserId(accessToken);
            tokenManager.deleteTokens(userId, accessToken);
        } catch (Exception e) {
            log.warn("로그아웃 중 토큰 처리 실패 (쿠키는 삭제됨): {}", e.getMessage());
        } finally {
            cookieProvider.expireTokenCookies(response);
        }
    }
}
