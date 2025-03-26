package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class VerificationController {

    private TokenService tokenService;

    // TODO SET PUBLIC KEY
    @GetMapping("/public-key")
    public String getPublicKey() {
        // Возвращаем публичный ключ как строку (в формате PEM)
        return "-----BEGIN PUBLIC KEY-----\n" +
                "YourPublicKeyHere\n" +
                "-----END PUBLIC KEY-----";
    }


    @GetMapping("user-details")
    public ResponseEntity<UserDetailsDto> getDetailsFromToken(
            @RequestHeader("Authorization") String authHeader
    ) throws AuthException {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(tokenService.getDetails(token));
    }
}
