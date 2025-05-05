package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.config.JwtProperties;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerificationController {

    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    @GetMapping("/public-key")
    public String getPublicKey() {
        return "-----BEGIN PUBLIC KEY-----\n" +
                jwtProperties.getPublicKey() +
                "-----END PUBLIC KEY-----";
    }


    @GetMapping("/user-details")
    public ResponseEntity<UserDetailsDto> getDetailsFromToken(
            @RequestHeader("Authorization") String authHeader
    ) throws AuthException {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(tokenService.getDetails(token));
    }
}
