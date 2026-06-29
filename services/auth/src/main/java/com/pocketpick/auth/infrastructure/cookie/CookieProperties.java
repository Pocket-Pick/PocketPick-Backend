package com.pocketpick.auth.infrastructure.cookie;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cookie")
public class CookieProperties {

    private final String sameSite;
    private final boolean secure;

    public CookieProperties(String sameSite, boolean secure) {
        this.sameSite = sameSite;
        this.secure = secure;
    }
}
