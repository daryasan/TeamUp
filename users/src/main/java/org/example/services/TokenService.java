package org.example.services;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.models.User;
import org.example.security.JwtProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
public class TokenService {

    private JwtProperties jwtProperties;

    public enum ClaimsEnum {
        email, id, nickname, role
    }

    // TODO null pointer
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            jwtProperties.getKey().getBytes()
    );

    String returnAccessToken(User user) {
        return generateAccessToken(user, new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()));
    }

    private String generateAccessToken(User user, Date expires) {
        Date issuedDate = new Date();
        return Jwts.builder()
                .claim(ClaimsEnum.email.name(), user.getEmail())
                .claim(ClaimsEnum.id.name(), user.getId())
                .claim(ClaimsEnum.nickname.name(), user.getNickname())
                .claim(ClaimsEnum.role.name(), user.getRole())
                .issuedAt(issuedDate)
                .expiration(expires)
                .signWith(secretKey).compact();
    }

    private io.jsonwebtoken.Claims getClaims(String token) throws AuthException {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();
            return parser
                    .parseSignedClaims(token)
                    .getPayload();
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
        String email;
        try {
            email = getEmailFromToken(token);
            return Objects.equals(userDetails.getUsername(), email) && !isExpired(token);
        } catch (AuthException e) {
            return false;
        }
    }
}
