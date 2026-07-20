package com.realmon.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    private int scanRequestsPerMinute = 6;
    private int generationRequestsPerMinute = 3;
}
