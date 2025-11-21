package com.servicemarketplace.api.config.CustomConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "brevo")
@Data
public class BrevoConfig {
    private String defaultSender;
    private String apiKey;
}
