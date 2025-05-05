package services;

import org.example.dto.UserDetailsDto;
import org.example.exceptions.AuthException;
import org.example.models.RolesEnum;
import org.example.models.User;
import org.example.config.JwtProperties;
import org.example.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.*;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    public void setUp() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        JwtProperties jwtProperties = new JwtProperties(publicKeyString, privateKeyString, 3600000L);

        tokenService = new TokenService(jwtProperties);
        ReflectionTestUtils.invokeMethod(tokenService, "initKeys");
    }

//    @Test
//    public void keys_exception() {
//        JwtProperties jwtProperties = new JwtProperties("", "", 3600000L);
//
//        tokenService = new TokenService(jwtProperties);
//        assertThrows(InvalidKeyException.class, ReflectionTestUtils.invokeMethod(tokenService, "initKeys"));
//    }


    @Test
    public void return_access_token_successful() {
        User user = new User();
        user.setId(123L);
        user.setEmail("test@example.com");
        user.setNickname("testnick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        assertNotNull(token);
        assertThat(token).isNotEmpty();
    }


    @Test
    public void get_email_from_token_valid() throws Exception {
        User user = new User();
        user.setId(234L);
        user.setEmail("email@example.com");
        user.setNickname("nick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        String email = tokenService.getEmailFromToken(token);

        assertEquals(user.getEmail(), email);
    }


    @Test
    public void get_nickname_from_token_valid() throws Exception {
        User user = new User();
        user.setId(345L);
        user.setEmail("nick@example.com");
        user.setNickname("nickname");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        String nickname = tokenService.getNicknameFromToken(token);

        assertEquals("nickname", nickname);
    }


    @Test
    public void get_user_id_from_token_valid() throws Exception {
        User user = new User();
        user.setId(456L);
        user.setEmail("id@example.com");
        user.setNickname("idnick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        Long userId = tokenService.getUserIdFromToken(token);

        assertEquals(user.getId(), userId);
    }


    @Test
    public void get_expiration_date_from_token_valid() throws Exception {
        User user = new User();
        user.setId(567L);
        user.setEmail("exp@example.com");
        user.setNickname("expnick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        Date expiration = tokenService.getExpirationDateFromToken(token);

        assertThat(expiration).isAfter(new Date());
    }


    @Test
    public void get_role_from_token_valid() throws Exception {
        User user = new User();
        user.setId(678L);
        user.setEmail("role@example.com");
        user.setNickname("rolenick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        String role = tokenService.getRoleFromToken(token);

        assertEquals(RolesEnum.participant.toString(), role);
    }


    @Test
    public void get_details_successful() throws Exception {
        User user = new User();
        user.setId(789L);
        user.setEmail("details@example.com");
        user.setNickname("detailnick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        UserDetailsDto details = tokenService.getDetails(token);

        assertEquals(789L, details.getId());
        assertEquals("details@example.com", details.getEmail());
        assertEquals("detailnick", details.getNickname());
        assertEquals(RolesEnum.participant.toString(), details.getRole());
    }


    @Test
    public void get_details_expired_throws_exception() {
        User user = new User();
        user.setId(890L);
        user.setEmail("expired@example.com");
        user.setNickname("expnick");
        user.setRoleId(RolesEnum.participant);

        Date pastDate = new Date(System.currentTimeMillis() - 1000);
        String expiredToken = ReflectionTestUtils.invokeMethod(tokenService, "generateAccessToken", user, pastDate);

        assertThrows(AuthException.class, () -> tokenService.getDetails(expiredToken));
    }


    @Test
    public void is_valid_returns_true() {
        User user = new User();
        user.setId(901L);
        user.setEmail("valid@example.com");
        user.setNickname("validnick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User("validnick", "pass", List.of());

        boolean valid = tokenService.isValid(token, userDetails);

        assertTrue(valid);
    }



    @Test
    public void is_valid_returns_false_when_email_mismatch() {
        User user = new User();
        user.setId(902L);
        user.setEmail("valid@example.com");
        user.setNickname("validnick");
        user.setRoleId(RolesEnum.participant);

        String token = tokenService.returnAccessToken(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User("other@example.com", "pass", List.of());

        boolean valid = tokenService.isValid(token, userDetails);

        assertFalse(valid);
    }


    @Test
    public void get_claims_invalid_token_throws_auth_exception() {
        assertThrows(AuthException.class, () -> tokenService.getEmailFromToken("invalid.token.string"));
    }
}
