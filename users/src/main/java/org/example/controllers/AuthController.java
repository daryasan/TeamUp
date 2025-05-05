package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.User;
import org.example.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final Validator validator;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(
            @RequestBody RegisterDto registerDto,
            BindingResult bindingResult
    ) throws DataException, UserException, AuthException {

        validator.validate(registerDto, bindingResult);
        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);

    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @RequestBody LoginDto loginDto,
            BindingResult bindingResult
    ) throws AuthException, UserException, DataException {

        validator.validate(loginDto, bindingResult);
        return ResponseEntity.ok(authService.login(loginDto));

    }

//    @PostMapping("/logout")
//    public ResponseEntity<HttpStatus> logout(
//            @RequestBody String token,
//            BindingResult bindingResult) {
//
//        validator.validate(token, bindingResult);
//        return ResponseEntity.ok(HttpStatus.OK);
//
//    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<HttpStatus> deactivateAccount(
            @RequestHeader("Authorization") String authHeader
    ) throws AuthException, UserException {
        String token = authHeader.substring(7);
        authService.deactivateAccount(token);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PatchMapping("/change-password")
    public ResponseEntity<User> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordDto changePasswordDto
    ) throws DataException, AuthException, UserException {
        String token = authHeader.substring(7);
        return new ResponseEntity<>(authService.changePassword(token, changePasswordDto), HttpStatus.OK);

    }

    @PatchMapping("/change-email")
    public ResponseEntity<TokenResponseDto> changeEmail(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangeEmailDto changeEmailDto
    ) throws DataException, AuthException, UserException {
        String token = authHeader.substring(7);
        changeEmailDto.setToken(token);
        return new ResponseEntity<>(authService.changeEmail(changeEmailDto), HttpStatus.OK);

    }


}
