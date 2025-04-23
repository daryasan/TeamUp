package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.TagDto;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public User assignSkills(String token, List<TagDto> tags) throws AuthException, DataException {
        UserDetailsDto dto = tokenService.getDetails(token);
        Optional<User> user = userRepository.findById(dto.getId());
        if (user.isEmpty()) throw new DataException("Wrong token!");

        for (TagDto t : tags) {
            user.get().getTags().add(t.getId());
        }

        return user.get();

    }

}
