package org.example.controllers;

import org.example.dto.UserFullInfoDto;
import org.example.dto.UserInfoDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.User;
import org.example.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @PatchMapping("/add/")
    public ResponseEntity<User> addUserAdditionalInfo(
            @RequestBody UserInfoDto userInfoDto
    ) throws AuthException, UserException {

        return ResponseEntity.ok(userService.addAdditionalUserInfo(userInfoDto));

    }

    @GetMapping("/id={id}")
    public ResponseEntity<Optional<User>> getUserById(
            @PathVariable long id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @PatchMapping("/edit/")
    public ResponseEntity<User> changeUserInfo(
            @RequestBody UserFullInfoDto userFullInfoDto
    ) throws AuthException, UserException, DataException {

        return ResponseEntity.ok(userService.changeUserInfo(userFullInfoDto));

    }



}
