package org.example.controllers;

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
public class AuthController {

    private AuthService authService;
    private Validator validator;

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
    ) throws AuthException, UserException {

        validator.validate(loginDto, bindingResult);
        return ResponseEntity.ok(authService.login(loginDto));

    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(
            @RequestBody String token,
            BindingResult bindingResult) {

        validator.validate(token, bindingResult);
        authService.logout(token);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<HttpStatus> deactivateAccount(
            @RequestBody String token,
            BindingResult bindingResult
    ) throws AuthException, UserException {

        validator.validate(token, bindingResult);
        authService.deactivateAccount(token);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PatchMapping("/changePassword")
    public ResponseEntity<User> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto
    ) throws DataException, AuthException, UserException {

        return new ResponseEntity<>(authService.changePassword(changePasswordDto), HttpStatus.OK);

    }

    @PatchMapping("/changeEmail")
    public ResponseEntity<TokenResponseDto> changeEmail(
            @RequestBody ChangeEmailDto changeEmailDto
    ) throws DataException, AuthException, UserException {

        return new ResponseEntity<>(authService.changeEmail(changeEmailDto), HttpStatus.OK);

    }


}
