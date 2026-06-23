package com.pocketpick.user.domain.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateNotificationRequest(
        @NotNull Boolean notificationEnabled
) {
}
