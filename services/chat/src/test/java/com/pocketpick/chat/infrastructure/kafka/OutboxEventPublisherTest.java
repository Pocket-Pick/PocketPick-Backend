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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("PENDING 이벤트 발행")
    class PublishPendingEvents {

        @Test
        @DisplayName("PENDING 없으면 Kafka produce 호출하지 않는다")
        void publishPendingEvents_noPending_doesNotProduce() {
            given(outboxEventRepository.findAndMarkProcessing()).willReturn(Optional.empty());

            outboxEventPublisher.publishPendingEvents();

            verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
        }

        @Test
        @DisplayName("PENDING 선점 성공 시 Kafka produce 후 PUBLISHED로 업데이트한다")
        void publishPendingEvents_hasPending_producesAndMarksPublished() {
            OutboxEvent outboxEvent = buildOutboxEvent();
            given(outboxEventRepository.findAndMarkProcessing()).willReturn(Optional.of(outboxEvent));
            given(kafkaTemplate.send(anyString(), anyString(), any(ChatMessageEvent.class)))
                    .willReturn(CompletableFuture.completedFuture(new SendResult<>(null, null)));

            outboxEventPublisher.publishPendingEvents();

            verify(outboxEventRepository).save(outboxEvent);
            assertThat(outboxEvent.getStatus()).isEqualTo(OutboxStatus.PUBLISHED);
        }

        @Test
        @DisplayName("Kafka produce 실패 시 PENDING으로 복구한다")
        void publishPendingEvents_kafkaFails_marksPending() {
            OutboxEvent outboxEvent = buildOutboxEvent();
            CompletableFuture<SendResult<String, ChatMessageEvent>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new ExecutionException("Kafka down", new RuntimeException()));

            given(outboxEventRepository.findAndMarkProcessing()).willReturn(Optional.of(outboxEvent));
            given(kafkaTemplate.send(anyString(), anyString(), any(ChatMessageEvent.class)))
                    .willReturn(failedFuture);

            outboxEventPublisher.publishPendingEvents();

            verify(outboxEventRepository).save(outboxEvent);
            assertThat(outboxEvent.getStatus()).isEqualTo(OutboxStatus.PENDING);
        }
    }

    @Nested
    @DisplayName("Stuck 이벤트 복구")
    class RecoverStuckEvents {

        @Test
        @DisplayName("30초 이상 PROCESSING 상태인 이벤트를 PENDING으로 복구한다")
        void recoverStuckEvents_stuckExists_marksPending() {
            OutboxEvent stuckEvent = buildOutboxEvent();
            given(outboxEventRepository.findByStatusAndProcessingAtBefore(
                    any(OutboxStatus.class), any(LocalDateTime.class)))
                    .willReturn(List.of(stuckEvent));

            outboxEventPublisher.recoverStuckEvents();

            verify(outboxEventRepository).save(stuckEvent);
            assertThat(stuckEvent.getStatus()).isEqualTo(OutboxStatus.PENDING);
        }

        @Test
        @DisplayName("Stuck 이벤트 없으면 아무것도 하지 않는다")
        void recoverStuckEvents_noStuck_doesNothing() {
            given(outboxEventRepository.findByStatusAndProcessingAtBefore(
                    any(OutboxStatus.class), any(LocalDateTime.class)))
                    .willReturn(List.of());

            outboxEventPublisher.recoverStuckEvents();

            verify(outboxEventRepository, never()).save(any());
        }
    }

    private OutboxEvent buildOutboxEvent() {
        return OutboxEvent.from(new ChatMessageEvent(
                "msg-1", "room-1", 1L, 2L, "안녕하세요", MessageType.TEXT, LocalDateTime.now()
        ));
    }
}
