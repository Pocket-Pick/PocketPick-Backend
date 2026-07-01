package com.pocketpick.auth.domain.service;

import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;
import com.pocketpick.auth.domain.dto.ValidateCredentialsRequest;

public interface AccountUseCase {
    void validate(ValidateCredentialsRequest request);
    void createCredentials(CreateCredentialsRequest request);
}
