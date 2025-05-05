package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.TagDto;
import org.example.dto.UserFullInfoDto;
import org.example.dto.UserInfoDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.User;
import org.example.services.TagService;
import org.example.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TagService tagService;
    private final PhotoController photoController;


    @PatchMapping("/add-info")
    public ResponseEntity<User> addUserAdditionalInfo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserInfoDto userInfoDto
    ) throws UserException, DataException, AuthException, IOException {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(userService.addAdditionalUserInfo(token, userInfoDto));

    }

    @GetMapping("/id")
    public ResponseEntity<User> getUserById(
            @RequestParam long id
    ) throws UserException {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(
            @RequestParam String email
    ) throws DataException, UserException {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/nickname")
    public ResponseEntity<User> getUserByNickname(
            @RequestParam String nickname
    ) throws UserException, DataException {
        return ResponseEntity.ok(userService.getUserByNickname(nickname));
    }

    @GetMapping("/token")
    public ResponseEntity<User> getUserByToken(
            @RequestParam String token
    ) throws AuthException, UserException {
        return ResponseEntity.ok(userService.getUserByToken(token));
    }


    @PatchMapping("/edit")
    public ResponseEntity<User> changeUserInfo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserFullInfoDto userFullInfoDto
    ) throws AuthException, UserException, DataException, IOException {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(userService.changeUserInfo( token, userFullInfoDto));
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @RequestMapping("add-photo")
    public ResponseEntity<User> uploadProfilePhoto(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("photo") MultipartFile photo) throws IOException, AuthException, UserException {
        String token = authHeader.substring(7);
        String filename = photoController.uploadFile(photo);
        return ResponseEntity.ok(userService.uploadProfilePhoto(token, filename));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/all-by-role/")
    public ResponseEntity<List<User>> getUsersByRole(
            @RequestParam Integer roleId) throws DataException {
        return ResponseEntity.ok(userService.getUsersByRole(roleId));

    }


    @PatchMapping("add-tags")
    public ResponseEntity<User> assignTags(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody List<TagDto> tags
    ) throws DataException, AuthException {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(tagService.assignSkills(token, tags));
    }


}
