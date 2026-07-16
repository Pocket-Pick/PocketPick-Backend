package com.pocketpick.chat.infrastructure.kafka;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.outbox.OutboxEvent;
import com.pocketpick.chat.domain.outbox.OutboxEventRepository;
import com.pocketpick.chat.domain.outbox.OutboxStatus;
import com.pocketpick.chat.global.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    @Scheduled(fixedDelay = 200)
    public void publishPendingEvents() {
        Optional<OutboxEvent> outboxEventOpt = outboxEventRepository.findAndMarkProcessing();
        if (outboxEventOpt.isEmpty()) {
            return;
        }

        OutboxEvent outboxEvent = outboxEventOpt.get();
        try {
            ChatMessageEvent event = toChatMessageEvent(outboxEvent);
            kafkaTemplate.send(KafkaTopicConfig.CHAT_MESSAGE_TOPIC, outboxEvent.getRoomId(), event).get();
            outboxEvent.markAsPublished();
            outboxEventRepository.save(outboxEvent);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Outbox 재발행 실패: outboxEventId={}", outboxEvent.getId(), e);
            outboxEvent.markAsPending();
            outboxEventRepository.save(outboxEvent);
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void recoverStuckEvents() {
        List<OutboxEvent> stuckEvents = outboxEventRepository
                .findByStatusAndProcessingAtBefore(
                        OutboxStatus.PROCESSING,
                        LocalDateTime.now().minusSeconds(30)
                );

        stuckEvents.forEach(event -> {
            event.markAsPending();
            outboxEventRepository.save(event);
            log.warn("Stuck Outbox 이벤트 복구: outboxEventId={}", event.getId());
        });
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void deletePublishedEvents() {
        outboxEventRepository.deleteByStatusAndCreatedAtBefore(
                OutboxStatus.PUBLISHED,
                LocalDateTime.now().minusDays(7)
        );
    }

    private ChatMessageEvent toChatMessageEvent(OutboxEvent outboxEvent) {
        return new ChatMessageEvent(
                null,
                outboxEvent.getRoomId(),
                outboxEvent.getSenderId(),
                outboxEvent.getReceiverId(),
                outboxEvent.getContent(),
                outboxEvent.getType(),
                outboxEvent.getCreatedAt()
        );
    }
}
