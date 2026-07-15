package com.pocketpick.chat.domain.outbox;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxEventRepository extends MongoRepository<OutboxEvent, String> {

    List<OutboxEvent> findByStatus(OutboxStatus status);

    void deleteByStatusAndCreatedAtBefore(OutboxStatus status, LocalDateTime threshold);
}
