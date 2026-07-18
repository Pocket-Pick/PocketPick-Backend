package com.pocketpick.user.domain.event;

public record FcmTokenRegisteredEvent(Long userId, String fcmToken) {}
