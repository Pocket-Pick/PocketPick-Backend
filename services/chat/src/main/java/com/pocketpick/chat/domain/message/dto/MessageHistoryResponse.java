package com.pocketpick.chat.domain.message.dto;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public record MessageHistoryResponse(
        List<MessageItem> messages,
        String nextCursor,
        boolean hasNext
) {
    public record MessageItem(
            String messageId,
            Long senderId,
            String content,
            MessageType type,
            LocalDateTime createdAt
    ) {
        public static MessageItem from(ChatMessage message) {
            return new MessageItem(
                    message.getId(),
                    message.getSenderId(),
                    message.getContent(),
                    message.getType(),
                    message.getCreatedAt()
            );
        }
    }
}
