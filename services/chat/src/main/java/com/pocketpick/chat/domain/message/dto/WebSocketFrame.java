package com.pocketpick.chat.domain.message.dto;

import com.pocketpick.chat.domain.message.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebSocketFrame {

    private WebSocketFrameType frameType;
    private String roomId;
    private Long receiverId;
    private String content;
    private MessageType type;
}
