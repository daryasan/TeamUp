import org.example.dto.TagDto;
import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.TagService;
import org.example.services.TokenService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Test
    public void assign_skills_successful() throws AuthException, DataException {
        TagDto tag1 = new TagDto();
        tag1.setId(101L);
        TagDto tag2 = new TagDto();
        tag2.setId(102L);
        List<TagDto> tags = List.of(tag1, tag2);


        UserDetailsDto dto = new UserDetailsDto();
        dto.setId(1L);


        when(tokenService.getDetails("validToken")).thenReturn(dto);


        User user = new User();
        user.setTags(new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        User result = tagService.assignSkills("validToken", tags);


        assertThat(result.getTags()).containsExactlyInAnyOrder(101L, 102L);


        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void assign_skills_wrong_token() throws AuthException {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setId(2L);


        when(tokenService.getDetails("invalidToken")).thenReturn(dto);


        when(userRepository.findById(2L)).thenReturn(Optional.empty());


        assertThrows(DataException.class, () -> tagService.assignSkills("invalidToken", new ArrayList<>()));


        verifyNoMoreInteractions(userRepository);
    }
}
