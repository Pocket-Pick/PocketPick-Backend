package com.pocketpick.user.domain.service;

import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UpdateNotificationRequest;
import com.pocketpick.user.domain.dto.UpdateProfileRequest;
import com.pocketpick.user.domain.dto.UserResponse;

public interface UserUseCase {
    UserResponse register(RegisterRequest request);
    UserResponse getUser(Long userId);
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);
    UserResponse updateNotification(Long userId, UpdateNotificationRequest request);
}
