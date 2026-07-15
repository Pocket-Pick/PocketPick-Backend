package com.pocketpick.chat.domain.outbox;

import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "outbox_events")
public class OutboxEvent {

    @Id
    private String id;
    private String roomId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;

    @Indexed
    private OutboxStatus status;

    @Builder
    private OutboxEvent(String roomId, Long senderId, Long receiverId,
                        String content, MessageType type, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
        this.createdAt = createdAt;
        this.status = OutboxStatus.PENDING;
    }

    public static OutboxEvent from(ChatMessageEvent event) {
        return OutboxEvent.builder()
                .roomId(event.getRoomId())
                .senderId(event.getSenderId())
                .receiverId(event.getReceiverId())
                .content(event.getContent())
                .type(event.getType())
                .createdAt(event.getCreatedAt())
                .build();
    }

    public void markAsPublished() {
        this.status = OutboxStatus.PUBLISHED;
    }
}
