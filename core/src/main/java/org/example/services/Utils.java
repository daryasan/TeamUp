package org.example.services;

import org.example.dto.UserDetailsFromTokenDto;
import org.example.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class Utils {

    private RestTemplate restTemplate;

    final private static Pattern weblinkPattern =
            Pattern.compile("^https://+$");

    public static boolean verifyWebLink(String link) {
        return link.matches(link);
    }

    public boolean isParticipant(UserDetailsFromTokenDto user){
        return user.getRole().toLowerCase().contains("participant");
    }

    public boolean isParticipant(Long userId){
        return Objects.requireNonNull(getUserById(userId).getBody()).getRole()
                .toLowerCase().contains("participant");
    }

    public boolean isMentor(UserDetailsFromTokenDto user){
        return user.getRole().toLowerCase().contains("mentor");
    }

    public boolean isMentor(long userId){
        return Objects.requireNonNull(getUserById(userId).getBody()).getRole()
                .toLowerCase().contains("mentor");
    }

    public boolean isOrganizer(UserDetailsFromTokenDto user){
        return user.getRole().toLowerCase().contains("organizer");
    }

    public boolean isOrganizer(Long userId){
        return Objects.requireNonNull(getUserById(userId).getBody()).getRole()
                .toLowerCase().contains("organizer");
    }

    public ResponseEntity<UserDto> getUserById(Long userId){
        return restTemplate
                // TODO set port
                .getForEntity("http://localhost:8080/auth/id=" + userId,
                        UserDto.class);
    }

}
