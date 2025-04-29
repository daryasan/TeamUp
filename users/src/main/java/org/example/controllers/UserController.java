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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TagService tagService;

    @PatchMapping("/add/id={id}")
    public ResponseEntity<User> addUserAdditionalInfo(
            @PathVariable Long id,
            @RequestBody UserInfoDto userInfoDto
    ) throws UserException, DataException {

        return ResponseEntity.ok(userService.addAdditionalUserInfo(id, userInfoDto));

    }

    @GetMapping("/id={id}")
    public ResponseEntity<User> getUserById(
            @PathVariable long id
    ) throws UserException {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email={email}")
    public ResponseEntity<User> getUserByEmail(
            @PathVariable String email
    ) throws DataException, UserException {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/nickname={nickname}")
    public ResponseEntity<User> getUserByNickname(
            @PathVariable String nickname
    ) throws UserException, DataException {
        return ResponseEntity.ok(userService.getUserByNickname(nickname));
    }

    @GetMapping("/token={token}")
    public ResponseEntity<User> getUserByToken(
            @PathVariable String token
    ) throws AuthException, UserException {
        return ResponseEntity.ok(userService.getUserByToken(token));
    }


    @PatchMapping("/edit/id={id}")
    public ResponseEntity<User> changeUserInfo(
            @PathVariable Long id,
            @RequestBody UserFullInfoDto userFullInfoDto
    ) throws AuthException, UserException, DataException {

        return ResponseEntity.ok(userService.changeUserInfo(id, userFullInfoDto));

    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/all-by-role/role-id={roleId}")
    public ResponseEntity<List<User>> getUsersByRole(
            @PathVariable Integer roleId) throws DataException {

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
