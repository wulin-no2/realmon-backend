package com.realmon.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openai")
@Data
public class ChatGPTConfig {
    private String apikey;
    private String promptStarter;
    private String promptTemplate;
}
