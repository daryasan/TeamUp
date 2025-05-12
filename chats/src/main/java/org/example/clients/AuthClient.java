package org.example.clients;

import org.example.dto.UserDetailsFromTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "users-service-auth", url = "http://localhost:8080")
public interface AuthClient {

    @GetMapping("/public-key")
    String getPublicKey();

    @GetMapping("/user-details")
    UserDetailsFromTokenDto getDetailsFromToken();
}
