package com.pocketpick.chat.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "kafka.topic.chat-message")
public class KafkaTopicProperties {

    private final int partitions;
    private final int replicas;

    public KafkaTopicProperties(int partitions, int replicas) {
        this.partitions = partitions;
        this.replicas = replicas;
    }
}
