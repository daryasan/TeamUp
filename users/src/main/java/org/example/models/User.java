package org.example.models;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Roles role;
    private String image;
}
