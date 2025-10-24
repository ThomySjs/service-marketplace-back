package com.servicemarketplace.api.config.CustomConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "url")
@Data
public class UrlConfig {
    private String frontendUrl;
}
