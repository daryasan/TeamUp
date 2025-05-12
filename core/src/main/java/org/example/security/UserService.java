package org.example.security;

import org.example.dto.UserDetailsFromTokenDto;
import org.example.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class UserService {

    private final AuthClient authClient;

    public UserService(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Cacheable("publicKeys")
    public PublicKey getPublicKey() throws Exception {
        String key = authClient.getPublicKey();
        return JwtTokenValidator.getPublicKeyFromString(key);
    }

    @Cacheable("userDetails")
    public UserDetailsFromTokenDto getDetailsFromToken() {
        return authClient.getDetailsFromToken();
    }

    public UserDto getUserById(Long userId){
        return authClient.getUserById(userId);
    }

}