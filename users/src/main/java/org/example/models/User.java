package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.dto.TagDto;
import org.example.services.Utils;

import java.util.List;

@Data
@Entity
@Table(name = "user_")
@RequiredArgsConstructor
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
    @Convert(converter = Utils.RolesEnumConverter.class)
    private RolesEnum roleId;
    private String image;
//    @ManyToMany
//    @JoinTable(
//            name = "users_tags_",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
    private List<Long> tags;
}
