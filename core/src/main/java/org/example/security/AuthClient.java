package org.example.security;

import org.example.dto.UserDetailsFromTokenDto;
import org.example.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users-service", url = "http://users:8080")
public interface AuthClient {

    @GetMapping("/public-key")
    String getPublicKey();

    @GetMapping("/user-details")
    UserDetailsFromTokenDto getDetailsFromToken();

    @GetMapping("/user/id")
    UserDto getUserById(@RequestParam Long id);
}
