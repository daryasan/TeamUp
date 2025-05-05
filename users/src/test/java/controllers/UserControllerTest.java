package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.AuthApp;
import org.example.config.Config;
import org.example.controllers.PhotoController;
import org.example.dto.TagDto;
import org.example.dto.UserDetailsDto;
import org.example.dto.UserFullInfoDto;
import org.example.dto.UserInfoDto;
import org.example.models.User;
import org.example.services.TagService;
import org.example.services.TokenService;
import org.example.services.UserService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AuthApp.class, Config.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private TagService tagService;

    @MockBean
    private PhotoController photoController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void add_user_additional_info_valid() throws Exception {
        long userId = 1L;
        UserInfoDto userInfoDto = new UserInfoDto();

        User dummyUser = new User();
        dummyUser.setId(userId);
        dummyUser.setEmail("dummy@example.com");
        dummyUser.setNickname("dummy");

        given(userService.addAdditionalUserInfo(eq("dummy-token"), any(UserInfoDto.class)))
                .willReturn(dummyUser);

        mockMvc.perform(patch("/user/add-info")
                        .header("Authorization", "Bearer dummy-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    public void get_user_by_id_valid() throws Exception {
        Long userId = 2L;
        User dummyUser = new User();
        dummyUser.setId(userId);
        dummyUser.setEmail("user2@example.com");
        dummyUser.setNickname("user2");

        given(userService.getUserById(userId)).willReturn(dummyUser);

        mockMvc.perform(get("/user/id")
                        .param("id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    public void get_user_by_email_valid() throws Exception {
        String email = "user3@example.com";
        User dummyUser = new User();
        dummyUser.setId(3L);
        dummyUser.setEmail(email);
        dummyUser.setNickname("user3");

        given(userService.getUserByEmail(email)).willReturn(dummyUser);

        mockMvc.perform(get("/user/email")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    public void get_user_by_nickname_valid() throws Exception {
        String nickname = "user4";
        User dummyUser = new User();
        dummyUser.setId(4L);
        dummyUser.setEmail("user4@example.com");
        dummyUser.setNickname(nickname);

        given(userService.getUserByNickname(nickname)).willReturn(dummyUser);

        mockMvc.perform(get("/user/nickname")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(nickname));
    }

    @Test
    public void get_user_by_token_valid() throws Exception {
        String token = "dummy-token";
        User dummyUser = new User();
        dummyUser.setId(5L);
        dummyUser.setEmail("user5@example.com");
        dummyUser.setNickname("user5");

        given(userService.getUserByToken(token)).willReturn(dummyUser);

        mockMvc.perform(get("/user/token")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    public void change_user_info_valid() throws Exception {
        long userId = 6L;
        UserFullInfoDto userFullInfoDto = new UserFullInfoDto();

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("user6@example.com");
        updatedUser.setNickname("user6");

        given(userService.changeUserInfo(eq("token"), any(UserFullInfoDto.class)))
                .willReturn(updatedUser);

        mockMvc.perform(patch("/user/edit")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFullInfoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    public void upload_profile_photo_valid() throws Exception {
        String authHeader = "Bearer dummy-token";
        String filename = "photos/test.png";
        User dummyUser = new User();
        dummyUser.setId(11L);
        dummyUser.setEmail("user11@example.com");
        dummyUser.setNickname("user11");

        given(photoController.uploadFile(any())).willReturn(filename);
        given(userService.uploadProfilePhoto("dummy-token", filename)).willReturn(dummyUser);

        MockMultipartFile file = new MockMultipartFile("photo", "test.png", "image/png", "dummy image".getBytes());

        mockMvc.perform(multipart("/user/add-photo")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11));
    }

    @Test
    public void get_all_users_valid() throws Exception {
        User user1 = new User();
        user1.setId(7L);
        user1.setEmail("user7@example.com");
        user1.setNickname("user7");
        User user2 = new User();
        user2.setId(8L);
        user2.setEmail("user8@example.com");
        user2.setNickname("user8");
        List<User> users = Arrays.asList(user1, user2);

        given(userService.getAllUsers()).willReturn(users);

        mockMvc.perform(get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[1].id").value(8));
    }

    @Test
    public void get_users_by_role_valid() throws Exception {
        int roleId = 2;
        User user1 = new User();
        user1.setId(9L);
        user1.setEmail("user9@example.com");
        user1.setNickname("user9");
        List<User> users = List.of(user1);

        given(userService.getUsersByRole(roleId)).willReturn(users);

        mockMvc.perform(get("/user/all-by-role/")
                        .param("roleId", Integer.toString(roleId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(9));
    }

    @Test
    public void assign_tags_valid_assign_tags() throws Exception {
        String authToken = "Bearer dummy-token";

        TagDto tag1 = new TagDto();
        tag1.setId(1L);
        tag1.setName("Java");
        TagDto tag2 = new TagDto();
        tag2.setId(2L);
        tag2.setName("Spring");
        List<TagDto> tags = Arrays.asList(tag1, tag2);

        User dummyUser = new User();
        dummyUser.setId(10L);
        dummyUser.setEmail("user10@example.com");
        dummyUser.setNickname("user10");

        UserDetailsDto dto = new UserDetailsDto();
        dto.setId(10L);

        given(tagService.assignSkills("dummy-token", tags)).willReturn(dummyUser);
        given(tokenService.getDetails(anyString())).willReturn(dto);

        mockMvc.perform(patch("/user/add-tags")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tags)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }
}
