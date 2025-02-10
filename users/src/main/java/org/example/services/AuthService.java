package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.dto.TokenResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    // register user and add them to DB
    // return jwt token
    TokenResponseDto register(RegisterDto registerDto){
        // TODO
        return null;
    }


    // login user and return jwt token
    TokenResponseDto login(LoginDto loginDto){
        // TODO
        return null;
    }


    // logout user
    void logout () {
        // TODO
    }


    // delete user from DB
    void deactivateAccount(long userId){
        // TODO
    }
}
