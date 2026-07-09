package com.pocketpick.chat.domain.message.dto;

import com.pocketpick.chat.domain.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private String messageId;
    private String roomId;
    private Long senderId;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;

    public static ChatMessageResponse from(ChatMessageEvent event) {
        return new ChatMessageResponse(
                event.getMessageId(),
                event.getRoomId(),
                event.getSenderId(),
                event.getContent(),
                event.getType(),
                event.getCreatedAt()
        );
    }
}
