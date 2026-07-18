package com.pocketpick.chat.domain.message.dto;

public record ReadEvent(String roomId, Long readerId) {}
