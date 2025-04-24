package org.example.security;

import org.example.dto.UserDetailsFromTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// TODO change port
@FeignClient(name = "auth-service", url = "http://auth-service:8080/auth")
public interface AuthClient {

    @GetMapping("auth/public-key")
    String getPublicKey();

    @GetMapping("auth/user-details")
    UserDetailsFromTokenDto getDetailsFromToken();
}
