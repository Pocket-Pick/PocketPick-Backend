package com.pocketpick.chat.global.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String CHAT_MESSAGE_TOPIC = "chat.message";
    private static final int PARTITION_COUNT = 3;
    private static final int REPLICATION_FACTOR = 1;

    @Bean
    public NewTopic chatMessageTopic() {
        return TopicBuilder.name(CHAT_MESSAGE_TOPIC)
                .partitions(PARTITION_COUNT)
                .replicas(REPLICATION_FACTOR)
                .build();
    }
}
