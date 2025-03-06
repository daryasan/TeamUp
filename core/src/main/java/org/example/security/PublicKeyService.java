package org.example.security;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class PublicKeyService {

    private final PublicKeyClient publicKeyClient;

    public PublicKeyService(PublicKeyClient publicKeyClient) {
        this.publicKeyClient = publicKeyClient;
    }

    @Cacheable("publicKeys")
    public PublicKey getPublicKey() throws Exception {
        String key = publicKeyClient.getPublicKey();
        return JwtTokenValidator.getPublicKeyFromString(key);
    }
}