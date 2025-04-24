package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFullInfoDto {
    String nickname;
    String lastName;
    String firstName;
    String middleName;
    String contacts;
    String github;
    String experience;
    String description;
}
