package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserFullInfoDto;
import org.example.dto.UserInfoDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.Roles;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private TokenService tokenService;
    private UserRepository userRepository;

    // saves user to DB
    // throws 403 FORBIDDEN
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
    // throws 401 UNAUTHORIZED or 400 BAD REQUEST
    @Transactional
    public User addAdditionalUserInfo(UserInfoDto userInfoDto) throws UserException, AuthException {

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
        return newUser;
    }

    // adds user info OR rewrites it if exists.
    // gets more info as parameters compared to addUserInfo
    // throws 401 UNAUTHORIZED or 400 BAD REQUEST
    @Transactional
    public User changeUserInfo(UserFullInfoDto userFullInfoDto) throws AuthException, UserException, DataException {
        long id = tokenService.getUserIdFromToken(userFullInfoDto.getToken());
        Optional<User> exists = userRepository.findById(id);

        if (exists.isEmpty()){
            throw new UserException("User doesn't exist!");
        }

        if (!Utils.verifyGithub(userFullInfoDto.getGithub()))
            throw new DataException("Wrong link!");

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
        return newUser;
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


    // fetches user by token to optional user -> throws 401 UNAUTHORIZED
    // if wrong token
    Optional<User> getUserByToken(String token) throws AuthException {
        return userRepository.findById(
                tokenService.getUserIdFromToken(token)
        );

    }


    // fetches user by id to optional user -> doesn't throw
    // any exceptions
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }


    // fetches a collection of all users
    List<User> getAllUsers(){
        return userRepository.findAll();
    }


    // fetches all users with specified role
    List<User> getUsersByRole(Roles role){
        List<User> users = userRepository.findAll();
        List<User> usersByRole = new ArrayList<User>();
        for (User u : users){
            if (u.getRole() == role) usersByRole.add(u);
        }
        return usersByRole;
    }


    // deletes user from db
    // returns if attempt is successful
    boolean deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return false;
        userRepository.delete(user.get());
        return true;
    }


}
