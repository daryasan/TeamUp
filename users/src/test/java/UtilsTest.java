import org.example.exceptions.DataException;
import org.example.models.Role;
import org.example.models.RolesEnum;
import org.example.models.User;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.example.services.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UtilsTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    Utils utils;

    @Test
    public void is_email_unique_unique() {
        User user1 = new User();
        user1.setEmail("darya_smile17@mail.ru");
        User user2 = new User();
        user2.setEmail("desudakova@edu.hse.ru");
        String email = ("mrbeast@gmail.com");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));


        boolean res = utils.isEmailUnique(email);


        assertTrue(res);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);


    }

    @Test
    public void is_email_unique_email_not_unique() {
        User user1 = new User();
        user1.setEmail("darya_smile17@mail.ru");
        User user2 = new User();
        user2.setEmail("desudakova@edu.hse.ru");
        String email = ("desudakova@edu.hse.ru");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));


        boolean res = utils.isEmailUnique(email);


        assertFalse(res);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void verify_email_correct() {
        List<String> correct_emails = List.of(
                "email@example.com",
                "firstname.lastname@example.com",
                "email@subdomain.example.com",
                "firstname+lastname@example.com",
                "email@123.123.123.123",
                "1234567890@example.com",
                "email@example-one.com",
                "_______@example.com",
                "email@example.name",
                "email@example.museum",
                "email@example.co.jp",
                "firstname-lastname@example.com"
        );


        for (String email : correct_emails) {
            assertTrue(Utils.verifyEmail(email));
        }
    }

    @Test
    public void verify_mail_incorrect() {
        List<String> incorrect_emails = List.of(
                "plainaddress",
                "#@%^%#$@#$@#.com",
                "@example.com",
                "Joe Smith <email@example.com>",
                "email.example.com",
                "email@example@example.com",
                "あいうえお@example.com",
                "email@example.com (Joe Smith)",
                "email@example",
                "email@example..com"
        );


        for (String email : incorrect_emails) {
            assertFalse(Utils.verifyEmail(email));
        }
    }

    @Test
    public void verify_nickname_correct() {
        List<String> correct_nicknames = List.of(
                "john_doe",
                "alice.smith",
                "jack-123",
                "charlie_brown",
                "jane-doe",
                "samuel89",
                "cat_lady",
                "ninja.master",
                "batman_007",
                "johnny.bravo",
                "hero_1",
                "cool-guy",
                "super_mario",
                "master.mind",
                "player_2"
        );


        for (String nickname : correct_nicknames) {
            assertTrue(Utils.verifyNickname(nickname));
        }
    }

    @Test
    public void verify_nickname_incorrect() {
        List<String> incorrect_nicknames = List.of(
                "JohnDoe",    // содержит заглавные буквы
                "alice!smith", // содержит недопустимый символ "!"
                "jack@123",    // содержит недопустимый символ "@"
                "charlie_brown$", // содержит недопустимый символ "$"
                "jane?doe",    // содержит недопустимый символ "?"
                "samuel/89",   // содержит недопустимый символ "/"
                "cat lady",    // содержит пробел
                "ninja#master", // содержит недопустимый символ "#"
                "batman+007",  // содержит недопустимый символ "+"
                "johnny=bravo", // содержит недопустимый символ "="
                "hero<1>",     // содержит недопустимые символы "<" и ">"
                "cool.guy!",   // содержит недопустимый символ "!"
                "super_mario@", // содержит недопустимый символ "@"
                "master.mind:", // содержит недопустимый символ ":"
                "player*2"   // содержит недопустимый символ "*"
        );


        for (String email : incorrect_nicknames) {
            assertFalse(Utils.verifyNickname(email));
        }
    }

    @Test
    public void verify_password_correct() {
        List<String> correct_passwords = List.of(
                "Password1",
                "Secure123",
                "Admin2023",
                "HelloWorld9",
                "TestPass8",
                "AlphaBravo1",
                "Valid12345",
                "ThisIs1T",
                "Code123A",
                "Java2023!"
        );


        for (String password : correct_passwords) {
            assertTrue(Utils.verifyPassword(password));
        }
    }

    @Test
    public void verify_password_incorrect() {
        List<String> incorrect_passwords = List.of(
                "password",  // только строчные буквы, без цифр и заглавных букв
                "PASSWORD",  // только заглавные буквы, без строчных и цифр
                "12345678",  // только цифры, без букв
                "Short1",    // меньше 8 символов
                "noNumber",  // нет цифр
                "NOLOWER123", // нет строчных букв
                "noupper123", // нет заглавных букв
                "      ",     // пробелы
                "abcdefgh123" // только строчные и цифры, без заглавных
        );


        for (String password : incorrect_passwords) {
            assertFalse(Utils.verifyPassword(password));
        }
    }

    @Test
    public void verify_github_correct() {
        List<String> correct_github_urls = List.of(
                "https://github.com/johndoe",
                "https://github.com/alice-smith",
                "https://github.com/charlie123",
                "https://github.com/super-user",
                "https://github.com/my-profile",
                "https://github.com/username1",
                "https://github.com/coder_123",
                "https://github.com/tester7",
                "https://github.com/github-user",
                "https://github.com/example-profile"
        );


        for (String url : correct_github_urls) {
            assertTrue(Utils.verifyGithub(url));
        }
    }

    @Test
    public void verify_github_incorrect() {
        List<String> incorrect_github_urls = List.of(
                "http://github.com/johndoe",  // http вместо https
                "https://github.com/",       // отсутствует имя пользователя
                "https://github.com/john doe", // содержит пробел
                "https://github.com/@johndoe", // содержит недопустимый символ "@"
                "https://github.com/john%doe", // содержит недопустимый символ "%"
                "github.com/johndoe",        // отсутствует https://
                "https://gitlab.com/johndoe", // неверный домен
                "https://github.com//johndoe", // двойной слэш
                "https://github.com/johndoe!",  // содержит недопустимый символ "!"
                "https://github.com/daryasan/TeamUp"
        );


        for (String url : incorrect_github_urls) {
            assertFalse(Utils.verifyGithub(url));
        }
    }

    @Test
    public void is_nickname_unique_unique() {
        User user1 = new User();
        user1.setNickname("darya_san");
        User user2 = new User();
        user2.setNickname("desu_kova");
        String nickname = "mrbeast_9000";
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));


        boolean res = utils.isNicknameUnique(nickname);


        assertTrue(res);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void is_nickname_unique_nickname_not_unique() {
        User user1 = new User();
        user1.setNickname("darya_san");
        User user2 = new User();
        user2.setNickname("desu_dakova");
        String nickname = "desu_dakova";
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));


        boolean res = utils.isNicknameUnique(nickname);


        assertFalse(res);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void convert_role_id_to_role_correct_ids() throws DataException {
        when(roleRepository.findById(1)).thenReturn(Optional.of(new Role(1, "participant")));
        when(roleRepository.findById(2)).thenReturn(Optional.of(new Role(2, "mentor")));
        when(roleRepository.findById(3)).thenReturn(Optional.of(new Role(3, "organizer")));

        RolesEnum participant = utils.convertRoleIdToRole(1);
        RolesEnum mentor = utils.convertRoleIdToRole(2);
        RolesEnum organizer = utils.convertRoleIdToRole(3);


        assertThat(participant).isEqualTo(RolesEnum.participant);
        assertThat(mentor).isEqualTo(RolesEnum.mentor);
        assertThat(organizer).isEqualTo(RolesEnum.organizer);
        verify(roleRepository, times(3)).findById(anyInt());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void convert_role_id_to_role_incorrect_ids() {
        when(roleRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(DataException.class, () -> utils.convertRoleIdToRole(99));
        verify(roleRepository, times(1)).findById(anyInt());
        verifyNoMoreInteractions(roleRepository);
    }


    @Test
    public void convert_role_id_to_role_incorrect_role() {
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(new Role(4, "incorrect role")));

        Assertions.assertThrows(DataException.class, () -> utils.convertRoleIdToRole(4));
        verify(roleRepository, times(1)).findById(anyInt());
        verifyNoMoreInteractions(roleRepository);
    }


}
