package com.pocketpick.auth.domain.service;

import com.pocketpick.auth.domain.domain.Account;
import com.pocketpick.auth.domain.domain.exception.DuplicateEmailException;
import com.pocketpick.auth.domain.domain.exception.WeakPasswordException;
import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;
import com.pocketpick.auth.domain.dto.ValidateCredentialsRequest;
import com.pocketpick.auth.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public void validate(ValidateCredentialsRequest request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }
        if (request.password().length() < MIN_PASSWORD_LENGTH) {
            throw new WeakPasswordException();
        }
    }

    @Override
    @Transactional
    public void createCredentials(CreateCredentialsRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Account account = Account.create(request.email(), encodedPassword, request.userId());
        accountRepository.save(account);
    }
}
