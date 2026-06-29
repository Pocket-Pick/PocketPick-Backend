package com.pocketpick.user.infrastructure.outbox;

import com.pocketpick.user.domain.domain.OutboxEvent;
import com.pocketpick.user.domain.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventTransactionHelper {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public List<OutboxEvent> fetchUnpublished() {
        return outboxEventRepository.findByPublishedFalseAndFailedFalse();
    }

    @Transactional
    public void markPublished(Long eventId) {
        outboxEventRepository.findById(eventId).ifPresent(OutboxEvent::markPublished);
    }

    @Transactional
    public void incrementRetry(Long eventId) {
        outboxEventRepository.findById(eventId).ifPresent(OutboxEvent::incrementRetry);
    }
}
