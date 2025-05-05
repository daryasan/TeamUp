package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.DataException;
import org.example.models.Role;
import org.example.models.RolesEnum;
import org.example.models.User;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class Utils {

    final private static Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*(),.?\":{}|<>_-]{8,}$");
    final private static Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$");
    final private static Pattern nicknamePattern = Pattern.compile("^[a-z0-9._-]+$");
    final private static Pattern githubPattern = Pattern.compile("^https://github\\.com/[a-zA-Z0-9-_]+$");
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public boolean isEmailUnique(String email) {
        for (User u : userRepository.findAll()) {
            if (email.equals(u.getEmail())) return false;
        }
        return true;
    }

    public static boolean verifyEmail(String email) {
        return email.matches(emailPattern.pattern());
    }

    public static boolean verifyNickname(String nickname) {
        return nickname.matches(nicknamePattern.pattern());
    }

    public static boolean verifyPassword(String password) {
        return password.matches(passwordPattern.pattern());
    }


    // verifies GitHub link my the mask
    public static boolean verifyGithub(String githubLink) {
        return githubLink.matches(githubPattern.pattern());
    }


    // checks if username if unique
    public boolean isNicknameUnique(String nickname) {
        for (User u : userRepository.findAll()) {
            if (nickname.equals(u.getNickname())) return false;
        }
        return true;
    }


    public RolesEnum convertRoleIdToRole(Integer roleId) throws DataException {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty())
            throw new DataException("Wrong role id!");

        if (role.get().getName().toLowerCase().contains("participant")) {
            return RolesEnum.participant;
        } else if (role.get().getName().toLowerCase().contains("organizer")) {
            return RolesEnum.organizer;
        } else if (role.get().getName().toLowerCase().contains("mentor")) {
            return RolesEnum.mentor;
        }
        throw new DataException("Wrong role id!");
    }

    @Converter(autoApply = true)
    public static class RolesEnumConverter implements AttributeConverter<RolesEnum, Integer> {

        @Override
        public Integer convertToDatabaseColumn(RolesEnum role) {
            switch (role) {
                case participant -> {
                    return 1;
                }
                case mentor -> {
                    return 2;
                }
                case organizer -> {
                    return 3;
                }
                default -> {
                    return 0;
                }
            }
        }

        @Override
        public RolesEnum convertToEntityAttribute(Integer dbValue) {
            return dbValue == null ? null : RolesEnum.values()[dbValue - 1];
        }
    }


}
