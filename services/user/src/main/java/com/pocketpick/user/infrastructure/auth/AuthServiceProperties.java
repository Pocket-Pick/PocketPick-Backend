package com.pocketpick.user.infrastructure.auth;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "services.auth")
public class AuthServiceProperties {

    private final String url;

    public AuthServiceProperties(String url) {
        this.url = url;
    }
}
