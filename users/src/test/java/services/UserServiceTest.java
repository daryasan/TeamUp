package services;

import org.example.dto.UserFullInfoDto;
import org.example.dto.UserInfoDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.RolesEnum;
import org.example.models.User;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.example.services.TokenService;
import org.example.services.UserService;
import org.example.services.Utils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    TokenService tokenService;
    @Mock
    RoleRepository roleRepository;
    @Mock
    Utils utils;


    @Test
    public void save_correct_user() throws UserException {

        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        when(userRepository.save(any(User.class))).thenReturn(user);


        final User actual = userService.saveUser(user);


        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void save_same_email_user() {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));


        assertThrows(UserException.class, () -> userService.saveUser(user));
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void add_additional_user_info_success() throws UserException, DataException, AuthException {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setContacts("desudakova@edu.hse.ru");
        userInfoDto.setGithub("https://github.com/daryasan");
        userInfoDto.setExperience("Опыт");
        User newUser = new User();
        newUser.setEmail("darya_smile17@mail.ru");
        newUser.setPassword("qwerty");
        newUser.setNickname("dasha_san");
        newUser.setFirstName("Дарья");
        newUser.setLastName("Судакова");
        newUser.setContacts("desudakova@edu.hse.ru");
        newUser.setGithub("https://github.com/daryasan");
        newUser.setImage("path/to/image");
        newUser.setExperience("Опыт");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(newUser);


        User actual = userService.addAdditionalUserInfo("token", userInfoDto);


        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void add_additional_user_no_user_found() {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setContacts("desudakova@edu.hse.ru");
        userInfoDto.setGithub("https://github.com/daryasan");
        userInfoDto.setExperience("Опыт");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> userService.addAdditionalUserInfo("token", userInfoDto));
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void add_additional_user_wrong_git_link() {
        UserInfoDto dto1 = new UserInfoDto();
        dto1.setGithub("https://github.comdaryasan");
        UserInfoDto dto2 = new UserInfoDto();
        dto2.setGithub("https:/github.com/daryasan");
        UserInfoDto dto3 = new UserInfoDto();
        dto3.setGithub("https//github.com/daryasan");
        UserInfoDto dto4 = new UserInfoDto();
        dto4.setGithub("https:/github/daryasan");
        UserInfoDto dto5 = new UserInfoDto();
        dto5.setGithub("https://github.com/daryasan/TeamUp.git");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));


        assertThrows(DataException.class, () -> userService.addAdditionalUserInfo("token", dto1));
        assertThrows(DataException.class, () -> userService.addAdditionalUserInfo("token", dto2));
        assertThrows(DataException.class, () -> userService.addAdditionalUserInfo("token", dto3));
        assertThrows(DataException.class, () -> userService.addAdditionalUserInfo("token", dto4));
        assertThrows(DataException.class, () -> userService.addAdditionalUserInfo("token", dto5));
        verify(userRepository, times(5)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void change_user_info_successful() throws DataException, UserException, AuthException {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        UserFullInfoDto userInfoDto = new UserFullInfoDto();
        userInfoDto.setFirstName("Даша");
        userInfoDto.setContacts("desudakova@edu.hse.ru");
        userInfoDto.setGithub("https://github.com/daryasan");
        //userInfoDto.setImage("path/to/image");
        userInfoDto.setExperience("Опыт");
        userInfoDto.setNickname("dasha_san");
        userInfoDto.setLastName("Судакова");
        User newUser = new User();
        newUser.setFirstName("Даша");
        newUser.setEmail("darya_smile17@mail.ru");
        newUser.setPassword("qwerty");
        newUser.setNickname("dasha_san");
        newUser.setLastName("Судакова");
        newUser.setContacts("desudakova@edu.hse.ru");
        newUser.setGithub("https://github.com/daryasan");
        newUser.setImage("path/to/image");
        newUser.setExperience("Опыт");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(newUser);


        User actual = userService.changeUserInfo("token", userInfoDto);


        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void change_user_info_user_not_found() {
        UserFullInfoDto userInfoDto = new UserFullInfoDto();
        userInfoDto.setContacts("desudakova@edu.hse.ru");
        userInfoDto.setGithub("https://github.com/daryasan");
        //userInfoDto.setImage("path/to/image");
        userInfoDto.setExperience("Опыт");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> userService.changeUserInfo("token", userInfoDto));
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void change_user_info_wrong_git_link() {
        UserFullInfoDto dto1 = new UserFullInfoDto();
        dto1.setGithub("http://github.com/daryasan");
        dto1.setFirstName("Дарья");
        dto1.setLastName("Судакова");
        UserFullInfoDto dto2 = new UserFullInfoDto();
        dto2.setGithub("https:/github.com/daryasan");
        dto2.setFirstName("Дарья");
        dto2.setLastName("Судакова");
        UserFullInfoDto dto3 = new UserFullInfoDto();
        dto3.setGithub("https//github.com/daryasan");
        dto3.setFirstName("Дарья");
        dto3.setLastName("Судакова");
        UserFullInfoDto dto4 = new UserFullInfoDto();
        dto4.setGithub("https:/github/daryasan");
        dto4.setFirstName("Дарья");
        dto4.setLastName("Судакова");
        UserFullInfoDto dto5 = new UserFullInfoDto();
        dto5.setGithub("https://github.com/daryasan/TeamUp.git");
        dto5.setFirstName("Дарья");
        dto5.setLastName("Судакова");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));


        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto1));
        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto2));
        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto3));
        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto4));
        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto5));
        verify(userRepository, times(5)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void change_user_info_empty_fields() {
        UserFullInfoDto dto1 = new UserFullInfoDto();
        dto1.setFirstName("Дарья");
        UserFullInfoDto dto2 = new UserFullInfoDto();
        dto2.setLastName("Судакова");
        UserFullInfoDto dto3 = new UserFullInfoDto();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));


        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto1));
        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto2));
        assertThrows(DataException.class, () -> userService.changeUserInfo("token", dto3));
        verify(userRepository, times(3)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void get_user_by_email_all_correct() throws UserException, DataException {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));


        User actual = userService.getUserByEmail("darya_smile17@mail.ru");


        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void get_user_by_email_incorrect_email() {
        assertThrows(DataException.class, () -> userService.getUserByEmail("darya_smile17mail.ru"));
        verifyNoMoreInteractions(userRepository);

        assertThrows(DataException.class, () -> userService.getUserByEmail("darya_smile17@mailru"));
        verifyNoMoreInteractions(userRepository);

        assertThrows(DataException.class, () -> userService.getUserByEmail("mail.ru"));
        verifyNoMoreInteractions(userRepository);

        assertThrows(DataException.class, () -> userService.getUserByEmail("@@mail.ru"));
        verifyNoMoreInteractions(userRepository);

        assertThrows(DataException.class, () -> userService.getUserByEmail("@@mail.ru"));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_user_by_email_no_user_found() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> userService.getUserByEmail("darya_smile17@mail.ru"));
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_user_by_nickname_all_correct() throws UserException, DataException {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        when(userRepository.findByNickname(any(String.class))).thenReturn(Optional.of(user));


        User actual = userService.getUserByNickname("dasha_san");


        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findByNickname(any(String.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void get_user_by_nickname_incorrect_nickname() {
        assertThrows(DataException.class, () -> userService.getUserByNickname("aaaaaaaaaaaaa  0"));
        verifyNoMoreInteractions(userRepository);

        assertThrows(DataException.class, () -> userService.getUserByNickname("asda!_0"));
        verifyNoMoreInteractions(userRepository);

        assertThrows(DataException.class, () -> userService.getUserByNickname("Asss0&&"));
        verifyNoMoreInteractions(userRepository);

        assertThrows(DataException.class, () -> userService.getUserByNickname("@@"));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_user_by_nickname_no_user_found() {
        when(userRepository.findByNickname(any(String.class))).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> userService.getUserByNickname("dasha_san"));
        verify(userRepository, times(1)).findByNickname(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void get_user_by_token_all_correct() throws AuthException, UserException {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(tokenService.getUserIdFromToken(any(String.class))).thenReturn((new Random().nextLong()));


        User actual = userService.getUserByToken("some token");


        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void get_user_by_token_incorrect_token() throws AuthException {
        when(tokenService.getUserIdFromToken(any(String.class))).thenThrow(AuthException.class);


        assertThrows(AuthException.class, () -> userService.getUserByToken("Some incorrect token"));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_user_by_token_user_not_found() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> userService.getUserByToken("some token"));
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void get_user_by_id_all_correct() throws UserException {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));


        User actual = userService.getUserById((new Random()).nextLong());


        assertThat(actual).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void get_user_by_id_user_not_found() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> userService.getUserById((new Random()).nextLong()));
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_all_users_correct() {
        User user1 = new User();
        user1.setRoleId(RolesEnum.mentor);
        user1.setEmail("darya_smile17@mail.ru");
        user1.setPassword("qwerty");
        user1.setNickname("dasha_san");
        user1.setFirstName("Дарья");
        user1.setLastName("Судакова");
        User user2 = new User();
        user2.setRoleId(RolesEnum.organizer);
        user2.setEmail("aboba@gmail.com");
        user2.setPassword("1234");
        user2.setNickname("aboba_aboba.");
        user2.setFirstName("Иван");
        user2.setLastName("Иванов");
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);


        List<User> actual = userService.getAllUsers();


        assertThat(actual).usingRecursiveComparison().isEqualTo(users);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);

    }


    @Test
    public void get_all_users_empty_list() {
        when(userRepository.findAll()).thenReturn(List.of());


        List<User> actual = userService.getAllUsers();


        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of());
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void get_users_by_role_roles() throws DataException {
        User user1 = new User();
        user1.setRoleId(RolesEnum.mentor);
        user1.setEmail("darya_smile17@mail.ru");
        user1.setPassword("qwerty");
        user1.setNickname("dasha_san");
        user1.setFirstName("Дарья");
        user1.setLastName("Судакова");
        User user2 = new User();
        user2.setRoleId(RolesEnum.organizer);
        user2.setEmail("aboba@gmail.com");
        user2.setPassword("1234");
        user2.setNickname("aboba_aboba.");
        user2.setFirstName("Иван");
        user2.setLastName("Иванов");
        User user3 = new User();
        user3.setRoleId(RolesEnum.participant);
        user3.setEmail("cupcake@gmail.com");
        user3.setPassword("eastisup");
        user3.setNickname("ruby");
        user3.setFirstName("Екатерина");
        user3.setLastName("Иванова");
        List<User> users = List.of(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(users);
        when(utils.convertRoleIdToRole(1)).thenReturn(RolesEnum.participant);
        when(utils.convertRoleIdToRole(2)).thenReturn(RolesEnum.mentor);
        when(utils.convertRoleIdToRole(3)).thenReturn(RolesEnum.organizer);


        List<User> participants = userService.getUsersByRole(1);
        List<User> mentors = userService.getUsersByRole(2);
        List<User> organizers = userService.getUsersByRole(3);


        assertThat(participants).usingRecursiveComparison().isEqualTo(List.of(user3));
        assertThat(mentors).usingRecursiveComparison().isEqualTo(List.of(user1));
        assertThat(organizers).usingRecursiveComparison().isEqualTo(List.of(user2));
        verify(userRepository, times(3)).findAll();
        verifyNoMoreInteractions(userRepository);
        verify(utils, times(3)).convertRoleIdToRole(anyInt());
        verifyNoMoreInteractions(roleRepository);

    }

    @Test
    public void get_users_by_role_roles_empty() throws DataException {
        User user1 = new User();
        user1.setRoleId(RolesEnum.mentor);
        user1.setEmail("darya_smile17@mail.ru");
        user1.setPassword("qwerty");
        user1.setNickname("dasha_san");
        user1.setFirstName("Дарья");
        user1.setLastName("Судакова");
        User user2 = new User();
        user2.setRoleId(RolesEnum.organizer);
        user2.setEmail("aboba@gmail.com");
        user2.setPassword("1234");
        user2.setNickname("aboba_aboba.");
        user2.setFirstName("Иван");
        user2.setLastName("Иванов");
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(utils.convertRoleIdToRole(1)).thenReturn(RolesEnum.participant);


        List<User> participants = userService.getUsersByRole(1);


        assertThat(participants).usingRecursiveComparison().isEqualTo(List.of());
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
        verify(utils, times(1)).convertRoleIdToRole(anyInt());
        verifyNoMoreInteractions(roleRepository);
    }


    @Test
    public void get_users_by_role_incorrect_role_identifier() throws DataException {
        when(utils.convertRoleIdToRole(anyInt())).thenThrow(DataException.class);


        assertThrows(DataException.class, () -> userService.getUsersByRole(333));
        verifyNoMoreInteractions(userRepository);
        verify(utils, times(1)).convertRoleIdToRole(anyInt());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void delete_user_successful() {
        User user = new User();
        user.setRoleId(RolesEnum.mentor);
        user.setEmail("darya_smile17@mail.ru");
        user.setPassword("qwerty");
        user.setNickname("dasha_san");
        user.setFirstName("Дарья");
        user.setLastName("Судакова");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));


        boolean res = userService.deleteUser(0);


        assertThat(res).isEqualTo(true);
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void delete_user_unsuccessful() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());


        boolean res = userService.deleteUser(0);


        assertThat(res).isEqualTo(false);
        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void upload_profile_photo_successful() throws AuthException, UserException {

        User user = new User();
        user.setImage("oldImagePath");
        when(tokenService.getUserIdFromToken("validToken")).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        User updatedUser = userService.uploadProfilePhoto("validToken", "newImagePath");


        assertNotNull(updatedUser);
        assertEquals("newImagePath", updatedUser.getImage());
        verify(tokenService).getUserIdFromToken("validToken");
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void upload_profile_photo_user_not_found() throws AuthException {

        when(tokenService.getUserIdFromToken("someToken")).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> userService.uploadProfilePhoto("someToken", "photoPath"));
        verify(tokenService).getUserIdFromToken("someToken");
        verify(userRepository).findById(2L);
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void upload_profile_photo_invalid_token() throws AuthException {

        when(tokenService.getUserIdFromToken("badToken")).thenThrow(new AuthException("Invalid token"));


        assertThrows(AuthException.class, () -> userService.uploadProfilePhoto("badToken", "photoPath"));
        verify(tokenService).getUserIdFromToken("badToken");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void set_user_photo_valid_existing_user_returns_photo_path() throws AuthException, UserException {
        String token = "dummy-token";
        String photoPath = "photos/random.png";
        Long userId = 100L;
        User user = new User();
        user.setId(userId);


        when(tokenService.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        String result = userService.setUserPhoto(token, photoPath);


        assertEquals(photoPath, result);
        assertEquals(photoPath, user.getImage());
        verify(userRepository).save(user);
    }

    @Test
    public void set_user_photo_user_not_exists_throws_user_exception() throws AuthException {
        String token = "dummy-token";
        String photoPath = "photos/random.png";
        Long userId = 100L;


        when(tokenService.getUserIdFromToken(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        UserException exception = assertThrows(UserException.class, () -> userService.setUserPhoto(token, photoPath));
        assertEquals("User doesn't exist!", exception.getMessage());
    }


}
