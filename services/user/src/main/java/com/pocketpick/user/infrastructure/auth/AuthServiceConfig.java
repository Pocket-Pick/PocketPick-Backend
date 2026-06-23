package com.pocketpick.user.infrastructure.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class AuthServiceConfig {

    private final AuthServiceProperties authServiceProperties;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(authServiceProperties.getUrl())
                .build();
    }
}
