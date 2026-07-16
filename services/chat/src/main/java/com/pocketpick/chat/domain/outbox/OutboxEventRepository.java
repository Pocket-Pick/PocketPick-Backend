package com.pocketpick.chat.domain.outbox;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository extends MongoRepository<OutboxEvent, String>, OutboxEventRepositoryCustom {

    List<OutboxEvent> findByStatusAndProcessingAtBefore(OutboxStatus status, LocalDateTime threshold);

    void deleteByStatusAndCreatedAtBefore(OutboxStatus status, LocalDateTime threshold);
}
