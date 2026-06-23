package com.pocketpick.user.domain.dto;

import com.pocketpick.user.domain.domain.User;

public record UserResponse(
        Long id,
        String nickname,
        String profileImageUrl,
        String region,
        boolean notificationEnabled
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getProfile().getNickname(),
                user.getProfile().getProfileImageUrl(),
                user.getProfile().getRegion(),
                user.isNotificationEnabled()
        );
    }
}
