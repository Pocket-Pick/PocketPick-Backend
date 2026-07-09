package com.pocketpick.chat.infrastructure.kafka;

import com.pocketpick.chat.application.MessageDeliveryService;
import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.global.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {

    private final MessageDeliveryService messageDeliveryService;

    @KafkaListener(topics = KafkaTopicConfig.CHAT_MESSAGE_TOPIC, groupId = "chat-service")
    public void consume(ChatMessageEvent event) {
        log.info("Kafka consumed: roomId={}, receiverId={}", event.getRoomId(), event.getReceiverId());
        messageDeliveryService.deliver(event);
    }
}
