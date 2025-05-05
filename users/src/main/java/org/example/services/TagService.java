package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.TagDto;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.models.User;
import org.example.models.UserTagMapping;
import org.example.repositories.UserRepository;
import org.example.repositories.UserTagMappingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final UserRepository userRepository;
    private final UserTagMappingRepository userTagMappingRepository;
    private final TokenService tokenService;

    public User assignSkills(String token, List<TagDto> tags) throws AuthException, DataException {
        UserDetailsDto dto = tokenService.getDetails(token);
        Optional<User> userOpt = userRepository.findById(dto.getId());
        if (userOpt.isEmpty()) throw new DataException("Wrong token!");
        User user = userOpt.get();

        // userTagMappingRepository.deleteByIdUserId(user.getId());

        for (TagDto t : tags) {
            UserTagMapping mapping = new UserTagMapping(user.getId(), t.getId());
            userTagMappingRepository.save(mapping);
        }

        List<Long> tagIds = new ArrayList<>();
        for (TagDto t : tags) {
            tagIds.add(t.getId());
        }
        user.setTags(tagIds);
        userRepository.save(user);

        return user;
    }
}
