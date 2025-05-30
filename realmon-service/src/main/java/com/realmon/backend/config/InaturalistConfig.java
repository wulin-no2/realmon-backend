package com.realmon.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "inat")
@Data
public class InaturalistConfig {
    private String token;
}

