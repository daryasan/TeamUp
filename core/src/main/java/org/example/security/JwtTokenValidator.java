package org.example.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
public class JwtTokenValidator {

    private UserService userService;

    public static PublicKey getPublicKeyFromString(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    // TODO (SET PUBLIC KEY)
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(userService.getPublicKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}