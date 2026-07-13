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

        ChatMessageEvent event = new ChatMessageEvent(
                saved.getId(),
                saved.getRoomId(),
                saved.getSenderId(),
                request.getReceiverId(),
                saved.getContent(),
                saved.getType(),
                saved.getCreatedAt()
        );

        eventPublisher.publishEvent(event);
    }

    private void updateLastMessage(String roomId, String content) {
        chatRoomRepository.findById(roomId).ifPresent(room -> {
            room.updateLastMessage(content);
            chatRoomRepository.save(room);
        });
    }
}
