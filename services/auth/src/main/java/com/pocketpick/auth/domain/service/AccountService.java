package com.pocketpick.auth.domain.service;

import com.pocketpick.auth.domain.domain.Account;
import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;
import com.pocketpick.auth.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createCredentials(CreateCredentialsRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Account account = Account.create(request.email(), encodedPassword, request.userId());
        accountRepository.save(account);
    }
}
