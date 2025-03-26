package org.example.security;

import org.example.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// TODO change port
@FeignClient(name = "auth-service", url = "http://auth-service:8080/auth")
public interface PublicKeyClient {

    @GetMapping("/public-key")
    String getPublicKey();

    @GetMapping("/user-details")
    UserDetailsDto getDetailsFromToken();
}
