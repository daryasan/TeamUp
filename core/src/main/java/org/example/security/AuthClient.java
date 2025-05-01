package org.example.security;

import org.example.dto.UserDetailsFromTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "users-service", url = "http://localhost:8080") //path = "/users-service")
public interface AuthClient {

    @GetMapping("/public-key")
    String getPublicKey();

    @GetMapping("/user-details")
    UserDetailsFromTokenDto getDetailsFromToken();
}
