package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.Role;
import org.example.models.RolesEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    // Роль, никнейм, email-адрес, фамилия, имя, отчество, пароль
    int roleId;
    String nickname;
    String email;
    String firstName;
    String lastName;
    String middleName;
    String password;
}
