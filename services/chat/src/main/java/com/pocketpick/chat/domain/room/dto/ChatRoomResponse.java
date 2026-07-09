package com.pocketpick.chat.domain.room.dto;

import com.pocketpick.chat.domain.room.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomResponse(
        String roomId,
        Long buyerId,
        Long sellerId,
        Long salePostId,
        String lastMessage,
        LocalDateTime updatedAt
) {
    public static ChatRoomResponse from(ChatRoom room) {
        return new ChatRoomResponse(
                room.getId(),
                room.getBuyerId(),
                room.getSellerId(),
                room.getSalePostId(),
                room.getLastMessage(),
                room.getUpdatedAt()
        );
    }
}
