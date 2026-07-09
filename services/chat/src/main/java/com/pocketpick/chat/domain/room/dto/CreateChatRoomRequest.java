package com.pocketpick.chat.domain.room.dto;

import jakarta.validation.constraints.NotNull;

public record CreateChatRoomRequest(
        @NotNull Long buyerId,
        @NotNull Long sellerId,
        @NotNull Long salePostId
) {}
