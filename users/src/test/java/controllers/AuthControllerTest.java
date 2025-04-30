package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.AuthApp;
import org.example.config.Config;
import org.example.config.JwtProperties;
import org.example.dto.ChangeEmailDto;
import org.example.dto.ChangePasswordDto;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.dto.TokenResponseDto;
import org.example.models.User;
import org.example.security.SecurityConfig;
import org.example.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AuthApp.class, Config.class, SecurityConfig.class})
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void register_valid_register() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("Password1");
        registerDto.setNickname("testNick");
        registerDto.setFirstName("John");
        registerDto.setLastName("Doe");
        registerDto.setMiddleName("M");
        registerDto.setRoleId(1);


        TokenResponseDto tokenResponseDto = new TokenResponseDto("dummy-register-token");
        given(authService.register(any(RegisterDto.class))).willReturn(tokenResponseDto);


        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))


                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("dummy-register-token"));
    }

    @Test
    public void login_valid_login() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("user@example.com");
        loginDto.setPassword("Password1");


        TokenResponseDto tokenResponseDto = new TokenResponseDto("dummy-login-token");
        given(authService.login(any(LoginDto.class))).willReturn(tokenResponseDto);


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-login-token"));
    }

//    @Test
//    public void logout_valid_logout() throws Exception {
//        String token = "dummy-token";
//
//
//        mockMvc.perform(post("/auth/logout")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("\"" + token + "\""))
//
//
//                .andExpect(status().isOk());
//    }

    @Test
    public void deactivate_account_valid_deactivate() throws Exception {
        String token = "dummy-token";


        mockMvc.perform(delete("/auth/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + token + "\""))


                .andExpect(status().isOk());
    }

    @Test
    public void change_password_valid_change_password() throws Exception {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setToken("dummy-token");
        changePasswordDto.setOldPassword("oldPass");
        changePasswordDto.setNewPassword("NewPass1");


        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setNickname("nickname");

        given(authService.changePassword(any(ChangePasswordDto.class))).willReturn(user);


        mockMvc.perform(patch("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordDto)))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void change_email_valid_change_email() throws Exception {
        ChangeEmailDto changeEmailDto = new ChangeEmailDto();
        changeEmailDto.setToken("dummy-token");
        changeEmailDto.setNewEmail("new@example.com");


        TokenResponseDto tokenResponseDto = new TokenResponseDto("dummy-email-token");
        given(authService.changeEmail(any(ChangeEmailDto.class))).willReturn(tokenResponseDto);


        mockMvc.perform(patch("/auth/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeEmailDto)))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-email-token"));
    }
}
