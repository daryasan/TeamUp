package org.example.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Data
@AllArgsConstructor
public class JwtProperties {
    private String key;
    private Long accessTokenExpiration;
}
