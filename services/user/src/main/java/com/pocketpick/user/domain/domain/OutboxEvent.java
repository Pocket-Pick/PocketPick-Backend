package com.pocketpick.user.domain.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "outbox_events", indexes = {
        @Index(name = "idx_outbox_events_published", columnList = "published")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    private static final int MAX_RETRY_COUNT = 5;
    private static final String CREDENTIALS_CREATED = "CREDENTIALS_CREATED";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private boolean published = false;

    @Column(nullable = false)
    private int retryCount = 0;

    @Column(nullable = false)
    private boolean failed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static OutboxEvent forCredentialsCreated(Long userId, String email, String encodedPassword) {
        CredentialsCreatedPayload payload = new CredentialsCreatedPayload(userId, email, encodedPassword);
        OutboxEvent event = new OutboxEvent();
        event.eventType = CREDENTIALS_CREATED;
        event.payload = toJson(payload);
        event.createdAt = LocalDateTime.now();
        return event;
    }

    private static String toJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("OutboxEvent payload 직렬화 실패", e);
        }
    }

    public void markPublished() {
        this.published = true;
    }

    public void incrementRetry() {
        this.retryCount++;
        if (this.retryCount >= MAX_RETRY_COUNT) {
            this.failed = true;
        }
    }
}
