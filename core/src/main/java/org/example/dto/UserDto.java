package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String nickname;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String password;
    private String contacts;
    private String github;
    private String experience;
    private String description;
    private String roleId;
    private String image;
}
