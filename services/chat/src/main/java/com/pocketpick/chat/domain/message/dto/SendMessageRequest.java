package com.pocketpick.chat.domain.message.dto;

import com.pocketpick.chat.domain.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    private String roomId;
    private Long receiverId;
    private String content;
    private MessageType type;
}
