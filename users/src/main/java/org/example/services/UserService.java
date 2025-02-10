package org.example.services;

import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import org.example.dto.ChangePasswordDto;
import org.example.dto.TokenResponseDto;
import org.example.dto.UserFullInfoDto;
import org.example.dto.UserInfoDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.Roles;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    final private Pattern passwordPattern = Pattern.compile("^.*(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$");
    final private Pattern emailPattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$");

    // saves user to DB
    // throws 400 BAD REQUEST
    void saveUser(User user) throws UserException {
        Optional<User> exists = userRepository.findByEmail(user.getEmail());
        if (exists.isPresent()){
            throw new UserException("User is already present in DB!");
        }
        userRepository.save(user);
    }

    // adds user info OR rewrites it if exists
    // is supposed to use when suggesting to fill additional info
    // right after registration
    // throws 403 FORBIDDEN or 400 BAD REQUEST
    @Transactional
    void addAdditionalUserInfo(UserInfoDto userInfoDto) throws UserException, AuthException {

        long id = tokenService.getUserIdFromToken(userInfoDto.getToken());
        Optional<User> exists = userRepository.findById(id);

        if (exists.isEmpty()){
            throw new UserException("User doesn't exist!");
        }

        User newUser = exists.get();

        newUser.setContacts(userInfoDto.getContacts());
        newUser.setDescription(userInfoDto.getDescription());
        newUser.setExperience(userInfoDto.getExperience());
        newUser.setGithub(userInfoDto.getGithub());
        newUser.setImage(userInfoDto.getImage());

        userRepository.save(newUser);
    }

    // adds user info OR rewrites it if exists.
    // gets more info as parameters compared to addUserInfo
    // throws 403 FORBIDDEN or 400 BAD REQUEST
    @Transactional
    void changeUserInfo(UserFullInfoDto userFullInfoDto) throws AuthException, UserException {
        long id = tokenService.getUserIdFromToken(userFullInfoDto.getToken());
        Optional<User> exists = userRepository.findById(id);

        if (exists.isEmpty()){
            throw new UserException("User doesn't exist!");
        }

        User newUser = exists.get();

        newUser.setContacts(userFullInfoDto.getContacts());
        newUser.setDescription(userFullInfoDto.getDescription());
        newUser.setExperience(userFullInfoDto.getExperience());
        newUser.setGithub(userFullInfoDto.getGithub());
        newUser.setImage(userFullInfoDto.getImage());
        newUser.setNickname(userFullInfoDto.getNickname());
        newUser.setFirstName(userFullInfoDto.getFirstName());
        newUser.setLastName(userFullInfoDto.getLastName());
        newUser.setMiddleName(userFullInfoDto.getMiddleName());

        userRepository.save(newUser);

    }


    // fetches user by email to optional user -> doesn't throw
    // any exceptions
    Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    // fetches user by nickname to optional user -> doesn't throw
    // any exceptions
    Optional<User> getUserByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }


    // fetches user by token to optional user -> throws 403 FORBIDDEN
    // if wrong token
    Optional<User> getUserByToken(String token) throws AuthException {
        return userRepository.findById(
                tokenService.getUserIdFromToken(token)
        );

    }


    // fetches user by id to optional user -> doesn't throw
    // any exceptions
    Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }


    // changes user password, if it doesn't match the mask
    // throws 400 BAD REQUEST (if user not found),
    //        403 FORBIDDEN (if token invalid or )
    void changePassword(ChangePasswordDto changePasswordDto) throws UserException, AuthException, DataException {
        Optional<User> user = getUserByToken(changePasswordDto.getToken());
        if (user.isEmpty()){
            throw new UserException("No such user!");
        }
        if (!user.get().getPassword().equals(
                passwordEncoder.encode(changePasswordDto.getOldPassword()))
        ){
            throw new DataException("Wrong old password!");
        }
        if (!changePasswordDto.getNewPassword().matches(passwordPattern.pattern())){
            throw new DataException("New password must contain at least one capital letter and a number");
        }

        user.get().setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user.get());
    }


    // changes email as major login method
    TokenResponseDto changeEmail(String token, String newEmail){
        // TODO
        return null;
    }


    // fetches a collection of all users
    List<User> getAllUsers(){
        //TODO
        return null;
    }


    // fetches all users with specified role
    List<User> getUsersByRole(Roles role){
        // TODO
        return null;
    }


}
