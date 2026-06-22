package com.pocketpick.user.domain.service;

import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UserResponse;

public interface UserUseCase {
    UserResponse register(RegisterRequest request);
    UserResponse getUser(Long userId);
}
