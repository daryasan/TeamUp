package org.example.dto;

import lombok.Data;

@Data
public class UserDetailsDto {
    private long id;
    private String email;
    private String nickname;
    private String role;
}
