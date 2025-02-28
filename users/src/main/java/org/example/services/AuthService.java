package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private UserService userService;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;
    private SessionService sessionService;

    // register user and add them to DB
    // return jwt token
    // trows 400 BAD REQUEST (if nickname not unique | email or password doesn't fir mask)
    //       403 FORBIDDEN (if user already exists)
    @Transactional
    public TokenResponseDto register(RegisterDto registerDto) throws DataException, UserException, AuthException {

        if (!Utils.isEmailUnique(registerDto.getEmail()))
            throw new UserException("User already exists!");
        if (!Utils.isNicknameUnique(registerDto.getNickname()))
            throw new DataException("Nickname is not unique! Try again.");
        if (!Utils.verifyEmail(registerDto.getEmail()))
            throw new DataException("Email is wrong!");
        if (!Utils.verifyPassword(registerDto.getPassword()))
            throw new DataException("Password must contain at least one capital letter and a number.");

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user.setNickname(registerDto.getNickname());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setMiddleName(registerDto.getMiddleName());

        String token = tokenService.returnAccessToken(user);
        sessionService.createSession(token);

        return new TokenResponseDto(token);
    }


    // login user and return jwt token
    // throws 403 FORBIDDEN (if email is not registered OR password wrong)
    public TokenResponseDto login(LoginDto loginDto) throws UserException, AuthException, DataException {
        User user = userService.getUserByEmail(loginDto.getEmail());

        if (!user.getPassword().equals(passwordEncoder.encode(loginDto.getPassword())))
            throw new UserException("Wrong password");

        String token = tokenService.returnAccessToken(user);
        sessionService.createSession(token);

        return new TokenResponseDto(token);
    }


    // changes user password, if it doesn't match the mask
    // throws 400 BAD REQUEST (if password wrong),
    //        401 UNAUTHORIZED (if token invalid),
    //        403 FORBIDDEN (if user not found)
    public User changePassword(ChangePasswordDto changePasswordDto) throws UserException, AuthException, DataException {
        User user = userService.getUserByToken(changePasswordDto.getToken());
        if (!user.getPassword().equals(
                passwordEncoder.encode(changePasswordDto.getOldPassword()))
        ) {
            throw new DataException("Wrong old password!");
        }
        if (!Utils.verifyPassword(changePasswordDto.getNewPassword())) {
            throw new DataException("New password must contain at least one capital letter and a number.");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userService.saveUser(user);
        return user;
    }


    // changes email as major login method
    public TokenResponseDto changeEmail(ChangeEmailDto changeEmailDto) throws AuthException, UserException, DataException {
        User user = userService.getUserByToken(changeEmailDto.getToken());
        if (!Utils.verifyEmail(changeEmailDto.getNewEmail())) {
            throw new DataException("Email doesn't match mask.");
        }

        user.setEmail(changeEmailDto.getNewEmail());
        userService.saveUser(user);
        return new TokenResponseDto(tokenService.returnAccessToken(user));
    }


    // logout user
    public void logout(String token) {
        // TODO
    }


    // delete user from DB
    // throws 401 UNAUTHORIZED (if token invalid)
    //        403 FORBIDDEN (if user doesn't exist)
    public void deactivateAccount(String token) throws AuthException, UserException {
        long userId = tokenService.getUserIdFromToken(token);
        if (!userService.deleteUser(userId)) throw new UserException("User doesn't exist");
    }
}
