package com.pocketpick.auth.domain.service;

import com.pocketpick.auth.domain.domain.Account;
import com.pocketpick.auth.domain.domain.exception.AccountNotFoundException;
import com.pocketpick.auth.domain.domain.exception.InvalidPasswordException;
import com.pocketpick.auth.domain.dto.LoginRequest;
import com.pocketpick.auth.domain.repository.AccountRepository;
import com.pocketpick.auth.infrastructure.cookie.CookieProvider;
import com.pocketpick.auth.infrastructure.jwt.JwtProvider;
import com.pocketpick.auth.infrastructure.jwt.TokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenManager tokenManager;
    private final CookieProvider cookieProvider;

    @Override
    @Transactional(readOnly = true)
    public void login(LoginRequest request, HttpServletResponse response) {
        Account account = accountRepository.findByEmail(request.email())
                .orElseThrow(AccountNotFoundException::new);

        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new InvalidPasswordException();
        }

        String[] tokens = tokenManager.createTokens(account.getUserId());
        cookieProvider.addTokenCookies(response, tokens[0], tokens[1]);
    }

    @Override
    public void logout(String accessToken, HttpServletResponse response) {
        Long userId = jwtProvider.getUserId(accessToken);
        tokenManager.deleteTokens(userId, accessToken);
        cookieProvider.expireTokenCookies(response);
    }
}
