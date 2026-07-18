package com.pocketpick.chat.domain.message;

import com.pocketpick.chat.domain.message.dto.WebSocketFrame;

public interface MessageUseCase {
    void send(Long senderId, WebSocketFrame frame);
    void markAsRead(String roomId, Long readerId);
}
