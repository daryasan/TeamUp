package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserFullInfoDto;
import org.example.dto.UserInfoDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.DataException;
import org.example.exceptions.UserException;
import org.example.models.RolesEnum;
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
    private Utils utils;

    // saves user to DB
    // throws 403 FORBIDDEN
    public User saveUser(User user) throws UserException {
        Optional<User> exists = userRepository.findByEmail(user.getEmail());
        if (exists.isPresent()) {
            throw new UserException("User is already present in DB!");
        }
        userRepository.save(user);
        return user;
    }

    // adds user info OR rewrites it if exists
    // is supposed to use when suggesting to fill additional info
    // right after registration
    // throws 401 UNAUTHORIZED or 400 BAD REQUEST
    @Transactional
    public User addAdditionalUserInfo(Long id, UserInfoDto userInfoDto) throws UserException, DataException {

        Optional<User> exists = userRepository.findById(id);

        if (exists.isEmpty()) {
            throw new UserException("User doesn't exist!");
        }
        if (!Utils.verifyGithub(userInfoDto.getGithub())) throw new DataException("Wrong git link!");

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
    public User changeUserInfo(Long id, UserFullInfoDto userFullInfoDto) throws UserException, DataException {
        Optional<User> exists = userRepository.findById(id);

        if (exists.isEmpty()) throw new UserException("User doesn't exist!");
        if (userFullInfoDto.getFirstName() == null || userFullInfoDto.getLastName() == null)
            throw new DataException("First and last name can't be empty!");
        if (!Utils.verifyGithub(userFullInfoDto.getGithub())) throw new DataException("Wrong link!");

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
    public User getUserByEmail(String email) throws DataException, UserException {
        if (!Utils.verifyEmail(email)) throw new DataException("Wrong email!");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) throw new UserException("User does not exist!");
        else return user.get();
    }


    // fetches user by nickname to optional user -> doesn't throw
    // any exceptions
    public User getUserByNickname(String nickname) throws UserException, DataException {
        if (!Utils.verifyNickname(nickname)) throw new DataException("Wrong nickname!");
        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isEmpty()) throw new UserException("User does not exist!");
        else return user.get();
    }


    // fetches user by token to optional user -> throws 401 UNAUTHORIZED
    // if wrong token
    public User getUserByToken(String token) throws AuthException, UserException {
        Optional<User> user = userRepository.findById(
                tokenService.getUserIdFromToken(token)
        );
        if (user.isEmpty())
            throw new UserException("User does not exist!");
        else return user.get();
    }


    // fetches user by id to optional user -> doesn't throw
    // any exceptions
    public User getUserById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new UserException("User does not exist!");
        else return user.get();
    }


    // fetches a collection of all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    // fetches all users with specified role
    public List<User> getUsersByRole(int roleId) throws DataException {
        RolesEnum role = utils.convertRoleIdToRole(roleId);
        List<User> users = userRepository.findAll();
        List<User> usersByRole = new ArrayList<>();
        for (User u : users) {
            if (role == u.getRole()) usersByRole.add(u);
        }
        return usersByRole;
    }


    // deletes user from db
    // returns if attempt is successful
    public boolean deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return false;
        userRepository.delete(user.get());
        return true;
    }


}
