
import org.example.dto.ChangeEmailDto;
import org.example.dto.ChangePasswordDto;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.dto.TokenResponseDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.RolesEnum;
import org.example.models.User;
import org.example.services.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserService userService;

    @Mock
    TokenService tokenService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    SessionService sessionService;

    @Mock
    Utils utils;

    @Test
    public void register_invalid_email() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("invalid-email");
        dto.setPassword("ValidPass1");
        dto.setNickname("validnick");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("M");
        dto.setRoleId(1);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("invalid-email")).thenReturn(false);


            assertThrows(DataException.class, () -> authService.register(dto));


            utilsMock.verify(() -> Utils.verifyEmail("invalid-email"));
        }
    }


    @Test
    public void register_invalid_password() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("valid@example.com");
        dto.setPassword("badpass");
        dto.setNickname("validnick");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("M");
        dto.setRoleId(1);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("valid@example.com")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyPassword("badpass")).thenReturn(false);


            assertThrows(DataException.class, () -> authService.register(dto));


            utilsMock.verify(() -> Utils.verifyEmail("valid@example.com"));
            utilsMock.verify(() -> Utils.verifyPassword("badpass"));
        }
    }


    @Test
    public void register_invalid_nickname() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("valid@example.com");
        dto.setPassword("ValidPass1");
        dto.setNickname("BadNick!");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("M");
        dto.setRoleId(1);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("valid@example.com")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyPassword("ValidPass1")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyNickname("BadNick!")).thenReturn(false);


            assertThrows(DataException.class, () -> authService.register(dto));


            utilsMock.verify(() -> Utils.verifyEmail("valid@example.com"));
            utilsMock.verify(() -> Utils.verifyPassword("ValidPass1"));
            utilsMock.verify(() -> Utils.verifyNickname("BadNick!"));
        }
    }


    @Test
    public void register_email_not_unique() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("duplicate@example.com");
        dto.setPassword("ValidPass1");
        dto.setNickname("validnick");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("M");
        dto.setRoleId(1);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("duplicate@example.com")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyPassword("ValidPass1")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyNickname("validnick")).thenReturn(true);

            when(utils.isEmailUnique("duplicate@example.com")).thenReturn(false);


            assertThrows(UserException.class, () -> authService.register(dto));


            utilsMock.verify(() -> Utils.verifyEmail("duplicate@example.com"));
            utilsMock.verify(() -> Utils.verifyPassword("ValidPass1"));
            utilsMock.verify(() -> Utils.verifyNickname("validnick"));
        }
    }


    @Test
    public void register_nickname_not_unique() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("unique@example.com");
        dto.setPassword("ValidPass1");
        dto.setNickname("duplicateNick");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("M");
        dto.setRoleId(1);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("unique@example.com")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyPassword("ValidPass1")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyNickname("duplicateNick")).thenReturn(true);

            when(utils.isEmailUnique("unique@example.com")).thenReturn(true);
            when(utils.isNicknameUnique("duplicateNick")).thenReturn(false);


            assertThrows(DataException.class, () -> authService.register(dto));


            utilsMock.verify(() -> Utils.verifyEmail("unique@example.com"));
            utilsMock.verify(() -> Utils.verifyPassword("ValidPass1"));
            utilsMock.verify(() -> Utils.verifyNickname("duplicateNick"));
        }
    }


    @Test
    public void register_missing_first_or_last_name() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("unique@example.com");
        dto.setPassword("ValidPass1");
        dto.setNickname("validnick");
        dto.setFirstName("");
        dto.setLastName(null);
        dto.setMiddleName("M");
        dto.setRoleId(1);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("unique@example.com")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyPassword("ValidPass1")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyNickname("validnick")).thenReturn(true);

            when(utils.isEmailUnique("unique@example.com")).thenReturn(true);
            when(utils.isNicknameUnique("validnick")).thenReturn(true);


            assertThrows(DataException.class, () -> authService.register(dto));


            utilsMock.verify(() -> Utils.verifyEmail("unique@example.com"));
            utilsMock.verify(() -> Utils.verifyPassword("ValidPass1"));
            utilsMock.verify(() -> Utils.verifyNickname("validnick"));
        }
    }


    @Test
    public void register_successful() throws DataException, UserException, AuthException {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("unique@example.com");
        dto.setPassword("ValidPass1");
        dto.setNickname("validnick");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("M");
        dto.setRoleId(1);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("unique@example.com")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyPassword("ValidPass1")).thenReturn(true);
            utilsMock.when(() -> Utils.verifyNickname("validnick")).thenReturn(true);

            when(utils.isEmailUnique("unique@example.com")).thenReturn(true);
            when(utils.isNicknameUnique("validnick")).thenReturn(true);
            when(utils.convertRoleIdToRole(1)).thenReturn(RolesEnum.participant);

            User savedUser = new User();
            savedUser.setEmail("unique@example.com");
            savedUser.setPassword("encodedPass");
            savedUser.setNickname("validnick");
            savedUser.setFirstName("John");
            savedUser.setLastName("Doe");
            savedUser.setMiddleName("M");
            savedUser.setRole(RolesEnum.participant);

            when(userService.saveUser(any(User.class))).thenReturn(savedUser);
            when(tokenService.returnAccessToken(savedUser)).thenReturn("token123");
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");

            TokenResponseDto response = authService.register(dto);

            assertNotNull(response);
            assertEquals("token123", response.getToken());

            verify(userService).saveUser(any(User.class));
            verify(tokenService).returnAccessToken(savedUser);
            verify(sessionService).createSession("token123");
        }
    }


    @Test
    public void login_wrong_password() throws DataException, UserException {
        LoginDto dto = new LoginDto();
        dto.setEmail("user@example.com");
        dto.setPassword("wrongpass");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPass");

        when(userService.getUserByEmail("user@example.com")).thenReturn(user);
        when(passwordEncoder.encode("wrongpass")).thenReturn("differentEncoded");

        assertThrows(UserException.class, () -> authService.login(dto));

        verify(userService).getUserByEmail("user@example.com");
    }


    @Test
    public void login_successful() throws UserException, AuthException, DataException {
        LoginDto dto = new LoginDto();
        dto.setEmail("user@example.com");
        dto.setPassword("correctpass");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPass");

        when(userService.getUserByEmail("user@example.com")).thenReturn(user);
        when(passwordEncoder.encode("correctpass")).thenReturn("encodedPass");
        when(tokenService.returnAccessToken(user)).thenReturn("logintoken");

        TokenResponseDto response = authService.login(dto);

        assertNotNull(response);
        assertEquals("logintoken", response.getToken());

        verify(userService).getUserByEmail("user@example.com");
        verify(tokenService).returnAccessToken(user);
        verify(sessionService).createSession("logintoken");
    }


    @Test
    public void changePassword_wrong_old_password() throws AuthException, UserException {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setToken("token");
        dto.setOldPassword("oldPass");
        dto.setNewPassword("NewValidPass1");

        User user = new User();
        user.setPassword("encodedOldPass");

        when(userService.getUserByToken("token")).thenReturn(user);
        when(passwordEncoder.encode("oldPass")).thenReturn("differentEncoded");

        assertThrows(DataException.class, () -> authService.changePassword(dto));

        verify(userService).getUserByToken("token");
    }


    @Test
    public void changePassword_invalid_new_password() throws AuthException, UserException {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setToken("token");
        dto.setOldPassword("oldPass");
        dto.setNewPassword("badnew");

        User user = new User();
        user.setPassword("encodedOldPass");

        when(userService.getUserByToken("token")).thenReturn(user);
        when(passwordEncoder.encode("oldPass")).thenReturn("encodedOldPass");

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyPassword("badnew")).thenReturn(false);


            assertThrows(DataException.class, () -> authService.changePassword(dto));


            utilsMock.verify(() -> Utils.verifyPassword("badnew"));
        }
    }


    @Test
    public void changePassword_successful() throws UserException, AuthException, DataException {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setToken("token");
        dto.setOldPassword("oldPass");
        dto.setNewPassword("NewValidPass1");

        User user = new User();
        user.setPassword("encodedOldPass");

        when(userService.getUserByToken("token")).thenReturn(user);
        when(passwordEncoder.encode("oldPass")).thenReturn("encodedOldPass");
        when(passwordEncoder.encode("NewValidPass1")).thenReturn("encodedNewPass");
        when(userService.saveUser(user)).thenReturn(user);

        User result = authService.changePassword(dto);

        assertNotNull(result);
        assertEquals("encodedNewPass", result.getPassword());

        verify(userService).getUserByToken("token");
        verify(userService).saveUser(user);
    }


    @Test
    public void changeEmail_invalid_email() throws AuthException, UserException {
        ChangeEmailDto dto = new ChangeEmailDto();
        dto.setToken("token");
        dto.setNewEmail("bademail");

        when(userService.getUserByToken("token")).thenReturn(new User());

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("bademail")).thenReturn(false);


            assertThrows(DataException.class, () -> authService.changeEmail(dto));


            utilsMock.verify(() -> Utils.verifyEmail("bademail"));
        }
    }


    @Test
    public void changeEmail_successful() throws AuthException, UserException, DataException {
        ChangeEmailDto dto = new ChangeEmailDto();
        dto.setToken("token");
        dto.setNewEmail("new@example.com");

        User user = new User();
        user.setEmail("old@example.com");

        when(userService.getUserByToken("token")).thenReturn(user);

        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyEmail("new@example.com")).thenReturn(true);
            when(userService.saveUser(user)).thenReturn(user);
            when(tokenService.returnAccessToken(user)).thenReturn("newtoken");

            TokenResponseDto response = authService.changeEmail(dto);

            assertNotNull(response);
            assertEquals("newtoken", response.getToken());

            utilsMock.verify(() -> Utils.verifyEmail("new@example.com"));
        }
    }


    @Test
    public void logout_does_nothing() {
        authService.logout("token");
    }


    @Test
    public void deactivateAccount_user_not_found() throws AuthException {
        when(tokenService.getUserIdFromToken("token")).thenReturn(1L);
        when(userService.deleteUser(1L)).thenReturn(false);

        assertThrows(UserException.class, () -> authService.deactivateAccount("token"));

        verify(tokenService).getUserIdFromToken("token");
        verify(userService).deleteUser(1L);
    }


    @Test
    public void deactivateAccount_successful() throws AuthException, UserException {
        when(tokenService.getUserIdFromToken("token")).thenReturn(1L);
        when(userService.deleteUser(1L)).thenReturn(true);

        authService.deactivateAccount("token");

        verify(tokenService).getUserIdFromToken("token");
        verify(userService).deleteUser(1L);
    }
}
