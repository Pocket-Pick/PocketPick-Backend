package com.pocketpick.user.domain.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "outbox_events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

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
    private LocalDateTime createdAt;

    public static OutboxEvent create(String eventType, String payload) {
        OutboxEvent event = new OutboxEvent();
        event.eventType = eventType;
        event.payload = payload;
        event.createdAt = LocalDateTime.now();
        return event;
    }

    public void markPublished() {
        this.published = true;
    }
}
