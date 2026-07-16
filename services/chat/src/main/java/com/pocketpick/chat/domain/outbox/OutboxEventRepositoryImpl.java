package com.pocketpick.chat.domain.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class OutboxEventRepositoryImpl implements OutboxEventRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<OutboxEvent> findAndMarkProcessing() {
        Query query = Query.query(Criteria.where("status").is(OutboxStatus.PENDING));
        Update update = Update.update("status", OutboxStatus.PROCESSING)
                .set("processingAt", LocalDateTime.now());
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        return Optional.ofNullable(
                mongoTemplate.findAndModify(query, update, options, OutboxEvent.class)
        );
    }
}
