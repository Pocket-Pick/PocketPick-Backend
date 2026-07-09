package com.pocketpick.chat.domain.message;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "messages")
public class ChatMessage {

    @Id
    private String id;

    @Indexed
    private String roomId;

    private Long senderId;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    @Builder
    private ChatMessage(String roomId, Long senderId, String content, MessageType type) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.readAt = LocalDateTime.now();
    }
}
