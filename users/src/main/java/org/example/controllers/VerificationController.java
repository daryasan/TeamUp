package org.example.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class VerificationController {


    // TODO SET PUBLIC KEY
    @GetMapping("/public-key")
    public String getPublicKey() {
        // Возвращаем публичный ключ как строку (в формате PEM)
        return "-----BEGIN PUBLIC KEY-----\n" +
                "YourPublicKeyHere\n" +
                "-----END PUBLIC KEY-----";
    }
}
