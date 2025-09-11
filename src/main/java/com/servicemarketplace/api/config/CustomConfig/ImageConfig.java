package com.servicemarketplace.api.config.CustomConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "cloudinary")
@Data
public class ImageConfig {
    private String url;
    private String folder;
    private String defaultImage;
}
