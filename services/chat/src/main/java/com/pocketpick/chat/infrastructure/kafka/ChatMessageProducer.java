package com.pocketpick.chat.infrastructure.kafka;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import com.pocketpick.chat.global.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageProducer {

    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    public void send(ChatMessageEvent event) {
        kafkaTemplate.send(KafkaTopicConfig.CHAT_MESSAGE_TOPIC, event.getRoomId(), event);
    }
}
