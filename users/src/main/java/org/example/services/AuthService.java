package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.User;
import org.example.repositories.SessionRepository;
import org.example.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final Utils utils;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    // register user and add them to DB
    // return jwt token
    // trows 400 BAD REQUEST (if nickname not unique | email or password doesn't fir mask)
    //       403 FORBIDDEN (if user already exists)
    @Transactional
    public TokenResponseDto register(RegisterDto registerDto) throws DataException, UserException, AuthException {

        if (!Utils.verifyEmail(registerDto.getEmail()))
            throw new DataException("Email is wrong!");
        if (!Utils.verifyPassword(registerDto.getPassword()))
            throw new DataException("Password must contain at least one capital letter and a number.");
        if (!Utils.verifyNickname(registerDto.getNickname()))
            throw new DataException("Nickname is wrong!");
        if (!utils.isEmailUnique(registerDto.getEmail()))
            throw new UserException("User already exists!");
        if (!utils.isNicknameUnique(registerDto.getNickname()))
            throw new DataException("Nickname is not unique! Try again.");
        if (registerDto.getFirstName() == null ||
                registerDto.getFirstName().isEmpty() ||
                registerDto.getLastName() == null ||
                registerDto.getLastName().isEmpty())
            throw new DataException("Fill in first and last name");

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoleId(utils.convertRoleIdToRole(registerDto.getRoleId()));
        user.setNickname(registerDto.getNickname());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setMiddleName(registerDto.getMiddleName());

        userService.saveUser(user);

        String token = tokenService.returnAccessToken(user);
        sessionService.createSession(token);

        return new TokenResponseDto(token);
    }


    // login user and return jwt token
    // throws 403 FORBIDDEN (if email is not registered OR password wrong)
    public TokenResponseDto login(LoginDto loginDto) throws UserException, AuthException, DataException {
        User user = userService.getUserByEmail(loginDto.getEmail());

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new UserException("Wrong password");


        String token = tokenService.returnAccessToken(user);
        sessionService.createSession(token);

        return new TokenResponseDto(token);
    }


    // changes user password, if it doesn't match the mask
    // throws 400 BAD REQUEST (if password wrong),
    //        401 UNAUTHORIZED (if token invalid),
    //        403 FORBIDDEN (if user not found)
    public User changePassword(String token, ChangePasswordDto changePasswordDto) throws UserException, AuthException, DataException {
        User user = userService.getUserByToken(token);
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())
        ) {
            throw new DataException("Wrong old password!");
        }
        if (!Utils.verifyPassword(changePasswordDto.getNewPassword())) {
            throw new DataException("New password must contain at least one capital letter and a number.");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        return user;
    }


    // changes email as major login method
    public TokenResponseDto changeEmail(ChangeEmailDto changeEmailDto) throws AuthException, UserException, DataException {
        User user = userService.getUserByToken(changeEmailDto.getToken());
        if (!Utils.verifyEmail(changeEmailDto.getNewEmail())) {
            throw new DataException("Email doesn't match mask.");
        }

        user.setEmail(changeEmailDto.getNewEmail());
        userRepository.save(user);
        return new TokenResponseDto(tokenService.returnAccessToken(user));
    }


    @Transactional
    public void deactivateAccount(String token) throws AuthException, UserException {
        long userId = tokenService.getUserIdFromToken(token);
        sessionRepository.deleteByUserId(userId);
        if (!userService.deleteUser(userId)) throw new UserException("User doesn't exist");
    }
}
