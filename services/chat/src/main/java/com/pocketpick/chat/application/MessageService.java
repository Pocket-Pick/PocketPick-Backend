package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.ChatMessageRepository;
import com.pocketpick.chat.domain.message.MessageUseCase;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.message.dto.ReadEvent;
import com.pocketpick.chat.domain.message.dto.WebSocketFrame;
import com.pocketpick.chat.domain.room.ChatRoomRepository;
import com.pocketpick.chat.infrastructure.kafka.ChatMessageProducer;
import com.pocketpick.chat.infrastructure.websocket.WebSocketMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService implements MessageUseCase {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageProducer chatMessageProducer;
    private final WebSocketMessageSender webSocketMessageSender;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public void send(Long senderId, WebSocketFrame frame) {
        ChatMessage message = ChatMessage.builder()
                .roomId(frame.getRoomId())
                .senderId(senderId)
                .content(frame.getContent())
                .type(frame.getType())
                .build();

        ChatMessage saved = chatMessageRepository.save(message);

        updateLastMessage(frame.getRoomId(), frame.getContent());

        ChatMessageEvent event = ChatMessageEvent.builder()
                .messageId(saved.getId())
                .roomId(saved.getRoomId())
                .senderId(saved.getSenderId())
                .receiverId(frame.getReceiverId())
                .content(saved.getContent())
                .type(saved.getType())
                .createdAt(saved.getCreatedAt())
                .build();

        chatMessageProducer.send(event);
    }

    public void markAsRead(String roomId, Long readerId) {
        chatMessageRepository.findTop1ByRoomIdAndSenderIdNotAndReadAtIsNull(roomId, readerId)
                .ifPresent(message -> {
                    Query query = Query.query(Criteria.where("roomId").is(roomId)
                            .and("senderId").ne(readerId)
                            .and("readAt").isNull());
                    Update update = Update.update("readAt", LocalDateTime.now());
                    mongoTemplate.updateMulti(query, update, "messages");

                    webSocketMessageSender.sendReadEvent(message.getSenderId(), new ReadEvent(roomId, readerId));
                });
    }

    private void updateLastMessage(String roomId, String content) {
        chatRoomRepository.findById(roomId).ifPresent(room -> {
            room.updateLastMessage(content);
            chatRoomRepository.save(room);
        });
    }
}
