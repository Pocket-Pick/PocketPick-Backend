package com.pocketpick.chat.global.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "chat.server")
public class ChatServerProperties {

    private final String ip;
    private final int port;
}
