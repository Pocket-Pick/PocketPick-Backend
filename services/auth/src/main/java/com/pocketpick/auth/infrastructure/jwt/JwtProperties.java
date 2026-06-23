package com.pocketpick.auth.infrastructure.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String secret;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtProperties(String secret, long accessExpiration, long refreshExpiration) {
        this.secret = secret;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }
}
