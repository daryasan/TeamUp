package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.Roles;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    // Роль, никнейм, email-адрес, фамилия, имя, отчество, пароль
    Roles role;
    String nickname;
    String email;
    String lastName;
    String firstName;
    String middleName;
    String password;
}
