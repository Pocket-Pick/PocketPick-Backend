package com.pocketpick.chat.domain.room;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat_rooms")
public class ChatRoom {

    @Id
    private String id;
    private Long buyerId;
    private Long sellerId;
    private Long salePostId;
    private String lastMessage;
    private LocalDateTime updatedAt;

    @Builder
    private ChatRoom(Long buyerId, Long sellerId, Long salePostId) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.salePostId = salePostId;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLastMessage(String message) {
        this.lastMessage = message;
        this.updatedAt = LocalDateTime.now();
    }
}
