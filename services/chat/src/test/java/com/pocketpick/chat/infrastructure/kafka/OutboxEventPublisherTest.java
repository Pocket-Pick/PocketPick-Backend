package com.pocketpick.chat.infrastructure.kafka;

import com.pocketpick.chat.domain.message.MessageType;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.outbox.OutboxEvent;
import com.pocketpick.chat.domain.outbox.OutboxEventRepository;
import com.pocketpick.chat.domain.outbox.OutboxStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("OutboxEventPublisher")
@ExtendWith(MockitoExtension.class)
class OutboxEventPublisherTest {

    @Mock private OutboxEventRepository outboxEventRepository;
    @Mock private KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    @InjectMocks
    private OutboxEventPublisher outboxEventPublisher;

    @Nested
    @DisplayName("PENDING 이벤트 재발행")
    class PublishPendingEvents {

        @Test
        @DisplayName("PENDING 없으면 Kafka produce 호출하지 않는다")
        void publishPendingEvents_noPending_doesNotProduce() {
            given(outboxEventRepository.findByStatus(OutboxStatus.PENDING)).willReturn(List.of());

            outboxEventPublisher.publishPendingEvents();

            verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
        }

        @Test
        @DisplayName("PENDING 있으면 Kafka produce 후 PUBLISHED로 업데이트한다")
        void publishPendingEvents_hasPending_producesAndMarksPublished() {
            OutboxEvent outboxEvent = buildOutboxEvent();
            given(outboxEventRepository.findByStatus(OutboxStatus.PENDING)).willReturn(List.of(outboxEvent));
            given(kafkaTemplate.send(anyString(), anyString(), any(ChatMessageEvent.class)))
                    .willReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

            outboxEventPublisher.publishPendingEvents();

            verify(outboxEventRepository).save(outboxEvent);
        }

        @Test
        @DisplayName("Kafka 또 실패하면 PENDING 상태 유지한다")
        void publishPendingEvents_kafkaFails_keepsPending() {
            OutboxEvent outboxEvent = buildOutboxEvent();
            CompletableFuture<SendResult<String, ChatMessageEvent>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new ExecutionException("Kafka down", new RuntimeException()));

            given(outboxEventRepository.findByStatus(OutboxStatus.PENDING)).willReturn(List.of(outboxEvent));
            given(kafkaTemplate.send(anyString(), anyString(), any(ChatMessageEvent.class)))
                    .willReturn(failedFuture);

            outboxEventPublisher.publishPendingEvents();

            verify(outboxEventRepository, never()).save(any());
        }
    }

    private OutboxEvent buildOutboxEvent() {
        return OutboxEvent.from(new ChatMessageEvent(
                "msg-1", "room-1", 1L, 2L, "안녕하세요", MessageType.TEXT, LocalDateTime.now()
        ));
    }
}
