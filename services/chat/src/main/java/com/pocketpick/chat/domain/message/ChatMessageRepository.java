package com.pocketpick.chat.domain.message;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String roomId);

    List<ChatMessage> findByRoomIdAndIdLessThanOrderByIdDesc(String roomId, String cursor, org.springframework.data.domain.Pageable pageable);

    List<ChatMessage> findByRoomIdOrderByIdDesc(String roomId, org.springframework.data.domain.Pageable pageable);
}
