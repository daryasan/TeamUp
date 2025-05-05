package org.example.services;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.models.User;
import org.example.config.JwtProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProperties jwtProperties;

    public enum ClaimsEnum {
        email, id, nickname, role
    }

    private PrivateKey privateKey;
    private PublicKey publicKey;


    @PostConstruct
    private void initKeys() {
        this.privateKey = getPrivateKey(jwtProperties.getPrivateKey());
        this.publicKey = getPublicKey(jwtProperties.getPublicKey());
    }

    private String cleanKey(String key) {
        return key.replace("\n", "")
                .trim();
    }

    private PrivateKey getPrivateKey(String key) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(cleanKey(key));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading private key", e);
        }
    }

    private PublicKey getPublicKey(String key) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(cleanKey(key));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading public key", e);
        }
    }


    public String returnAccessToken(User user) {
        return generateAccessToken(user, new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()));
    }

    private String generateAccessToken(User user, Date expires) {
        Date issuedDate = new Date();
        return Jwts.builder()
                .claim(ClaimsEnum.email.name(), user.getEmail())
                .claim(ClaimsEnum.id.name(), user.getId())
                .claim(ClaimsEnum.nickname.name(), user.getNickname())
                .claim(ClaimsEnum.role.name(), user.getRoleId())
                .issuedAt(issuedDate)
                .expiration(expires)
                .signWith(privateKey)
                .compact();
    }

    private io.jsonwebtoken.Claims getClaims(String token) throws AuthException {
        try {
            JwtParser parser = Jwts.parser().
                    verifyWith(publicKey).
                    build();
            return parser.parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            throw new AuthException("Invalid token!");
        }
    }

    public String getEmailFromToken(String token) throws AuthException {
        return getClaims(token).get(ClaimsEnum.email.name()).toString();
    }

    public String getNicknameFromToken(String token) throws AuthException {
        return getClaims(token).get(ClaimsEnum.nickname.name()).toString();
    }

    public Long getUserIdFromToken(String token) throws AuthException {
        return parseLong(getClaims(token).get(ClaimsEnum.id.name()).toString());
    }

    public Date getExpirationDateFromToken(String token) throws AuthException {
        return getClaims(token).getExpiration();
    }

    public String getRoleFromToken(String token) throws AuthException {
        return getClaims(token).get(ClaimsEnum.role.name()).toString();
    }

    public UserDetailsDto getDetails(String token) throws AuthException {
        if (!isExpired(token)) {
            UserDetailsDto userDetailsDto = new UserDetailsDto();
            userDetailsDto.setId(getUserIdFromToken(token));
            userDetailsDto.setEmail(getEmailFromToken(token));
            userDetailsDto.setNickname(getNicknameFromToken(token));
            userDetailsDto.setRole(getRoleFromToken(token));
            return userDetailsDto;
        } else throw new AuthException("Expired token!");
    }

    private boolean isExpired(String token) throws AuthException {
        return getClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String nickname;
        try {
            nickname = getNicknameFromToken(token);
            return Objects.equals(userDetails.getUsername(), nickname) && !isExpired(token);
        } catch (AuthException e) {
            return false;
        }
    }
}
