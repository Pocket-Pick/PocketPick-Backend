package com.pocketpick.chat.global.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    public static final String CHAT_MESSAGE_TOPIC = "chat.message";

    private final KafkaTopicProperties kafkaTopicProperties;

    @Bean
    public NewTopic chatMessageTopic() {
        return TopicBuilder.name(CHAT_MESSAGE_TOPIC)
                .partitions(kafkaTopicProperties.getPartitions())
                .replicas(kafkaTopicProperties.getReplicas())
                .build();
    }
}
