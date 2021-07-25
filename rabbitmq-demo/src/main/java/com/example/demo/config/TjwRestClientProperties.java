package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="tjw")
@Data
public class TjwRestClientProperties {
    private String url;
    private String farmUrl;
    private String basePath;
}