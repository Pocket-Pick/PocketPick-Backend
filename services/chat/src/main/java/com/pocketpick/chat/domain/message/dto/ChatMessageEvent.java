package com.pocketpick.chat.domain.message.dto;

import com.pocketpick.chat.domain.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEvent {

    private String messageId;
    private String roomId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;
}
