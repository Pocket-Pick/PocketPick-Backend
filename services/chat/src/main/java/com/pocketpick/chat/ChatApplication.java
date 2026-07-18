package com.pocketpick.chat;

import com.pocketpick.chat.global.config.AwsProperties;
import com.pocketpick.chat.global.config.ChatServerProperties;
import com.pocketpick.chat.global.config.FirebaseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FirebaseProperties.class, AwsProperties.class, ChatServerProperties.class})
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
