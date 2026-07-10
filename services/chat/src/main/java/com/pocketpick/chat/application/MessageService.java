package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.ChatMessageRepository;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.message.dto.SendMessageRequest;
import com.pocketpick.chat.domain.room.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void send(Long senderId, SendMessageRequest request) {
        ChatMessage message = ChatMessage.builder()
                .roomId(request.getRoomId())
                .senderId(senderId)
                .content(request.getContent())
                .type(request.getType())
                .build();

        ChatMessage saved = chatMessageRepository.save(message);

        updateLastMessage(request.getRoomId(), request.getContent());

        ChatMessageEvent event = ChatMessageEvent.builder()
                .messageId(saved.getId())
                .roomId(saved.getRoomId())
                .senderId(saved.getSenderId())
                .receiverId(request.getReceiverId())
                .content(saved.getContent())
                .type(saved.getType())
                .createdAt(saved.getCreatedAt())
                .build();

        eventPublisher.publishEvent(event);
    }

    private void updateLastMessage(String roomId, String content) {
        chatRoomRepository.updateLastMessage(roomId, content);
    }
}
