package org.example.security;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", url = "http://auth-service:8080/auth")
public interface PublicKeyClient {

    @GetMapping("/public-key")
    String getPublicKey();
}
