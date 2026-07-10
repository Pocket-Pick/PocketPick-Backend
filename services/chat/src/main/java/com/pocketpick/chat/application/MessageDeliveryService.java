package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.infrastructure.fcm.FcmPushUseCase;
import com.pocketpick.chat.infrastructure.redis.OnlineStatusRepository;
import com.pocketpick.chat.infrastructure.websocket.WebSocketMessageSender;
import com.pocketpick.chat.presentation.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageDeliveryService {

    private final WebSocketMessageSender webSocketMessageSender;
    private final WebSocketSessionRegistry sessionRegistry;
    private final OnlineStatusRepository onlineStatusRepository;
    private final FcmPushUseCase fcmPushUseCase;
    private final MessageService messageService;

    public void deliver(ChatMessageEvent event) {
        Long receiverId = event.getReceiverId();

        if (onlineStatusRepository.isOnline(receiverId)) {
            webSocketMessageSender.send(receiverId, event);

            sessionRegistry.getCurrentRoom(receiverId)
                    .filter(roomId -> roomId.equals(event.getRoomId()))
                    .ifPresent(roomId -> messageService.markAsRead(roomId, receiverId));
        } else {
            fcmPushUseCase.sendPush(receiverId, event.getContent());
        }
    }
}
