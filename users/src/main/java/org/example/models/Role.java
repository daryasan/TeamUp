package org.example.models;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "role_")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
}
