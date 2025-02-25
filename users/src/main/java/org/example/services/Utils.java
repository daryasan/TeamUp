package org.example.services;

import org.example.models.User;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class Utils {

    final private static Pattern passwordPattern = Pattern.compile("^.*(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$");
    final private static Pattern emailPattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$");
    final private static Pattern githubPattern = Pattern.compile("/https:\\/\\/github\\.com\\/([^\\/]+)$");
    private static UserService userService;

    public static boolean isEmailUnique(String email){
        for (User u : userService.getAllUsers()){
            if (email.equals(u.getEmail())) return false;
        }
        return true;
    }

    public static boolean verifyEmail(String email){
        return email.matches(emailPattern.pattern());
    }

    public static boolean verifyPassword(String password){
        return password.matches(passwordPattern.pattern());
    }


    // verifies GitHub link my the mask
    public static boolean verifyGithub(String githubLink){
        return githubLink.matches(githubPattern.pattern());
    }


    // checks if username if unique
    public static boolean isNicknameUnique(String nickname){
       for (User u : userService.getAllUsers()){
           if (nickname.equals(u.getNickname())) return false;
       }
       return true;
    }



}
