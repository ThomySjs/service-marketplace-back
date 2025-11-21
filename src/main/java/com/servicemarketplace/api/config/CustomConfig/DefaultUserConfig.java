package com.servicemarketplace.api.config.CustomConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "default")
@Data
public class DefaultUserConfig {
    private String email;
    private String password;
    private String name;
}
