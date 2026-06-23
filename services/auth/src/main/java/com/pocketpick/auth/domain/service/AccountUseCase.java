package com.pocketpick.auth.domain.service;

import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;

public interface AccountUseCase {
    void createCredentials(CreateCredentialsRequest request);
}
