package com.pocketpick.chat.infrastructure.kafka;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.domain.outbox.OutboxEventRepository;
import com.pocketpick.chat.domain.outbox.OutboxStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@DisplayName("OutboxEventPublisher 정리")
@ExtendWith(MockitoExtension.class)
class OutboxEventCleanupTest {

    @Mock private OutboxEventRepository outboxEventRepository;
    @Mock private KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    @InjectMocks
    private OutboxEventPublisher outboxEventPublisher;

    @Nested
    @DisplayName("PUBLISHED 이벤트 삭제")
    class DeletePublishedEvents {

        @Test
        @DisplayName("7일 이상 된 PUBLISHED 건을 삭제한다")
        void deletePublishedEvents_deletesOlderThan7Days() {
            ArgumentCaptor<LocalDateTime> thresholdCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

            outboxEventPublisher.deletePublishedEvents();

            verify(outboxEventRepository).deleteByStatusAndCreatedAtBefore(
                    eq(OutboxStatus.PUBLISHED),
                    thresholdCaptor.capture()
            );

            LocalDateTime threshold = thresholdCaptor.getValue();
            assertThat(threshold).isBefore(LocalDateTime.now().minusDays(6));
            assertThat(threshold).isAfter(LocalDateTime.now().minusDays(8));
        }
    }
}
