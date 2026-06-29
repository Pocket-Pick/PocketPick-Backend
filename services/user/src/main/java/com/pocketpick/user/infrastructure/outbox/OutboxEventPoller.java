package com.pocketpick.user.infrastructure.outbox;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpick.user.domain.domain.OutboxEvent;
import com.pocketpick.user.domain.repository.OutboxEventRepository;
import com.pocketpick.user.infrastructure.auth.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final AuthServiceClient authServiceClient;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void poll() {
        List<OutboxEvent> events = outboxEventRepository.findByPublishedFalse();

        for (OutboxEvent event : events) {
            try {
                process(event);
                event.markPublished();
            } catch (Exception e) {
                log.warn("OutboxEvent 처리 실패 id={}, type={}", event.getId(), event.getEventType(), e);
            }
        }
    }

    private void process(OutboxEvent event) throws Exception {
        if ("CREDENTIALS_CREATED".equals(event.getEventType())) {
            Map<String, Object> payload = objectMapper.readValue(
                    event.getPayload(), new TypeReference<>() {});

            Long userId = ((Number) payload.get("userId")).longValue();
            String email = (String) payload.get("email");
            String encodedPassword = (String) payload.get("encodedPassword");

            authServiceClient.createCredentials(userId, email, encodedPassword);
        }
    }
}
