package org.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@ConfigurationProperties("jwt")
@Data
@AllArgsConstructor
public class JwtProperties {
    private final String publicKey;
    private final String privateKey;
    private final Long accessTokenExpiration;
}
