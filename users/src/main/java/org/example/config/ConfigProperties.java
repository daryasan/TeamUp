package org.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties("files")
public class ConfigProperties {
    private String keyId;
    private String secretKey;
    private String region;
    private String s3_endpoint;
    private String bucket;
}
