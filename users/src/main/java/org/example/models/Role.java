package org.example.models;


import lombok.*;

import javax.persistence.*;

@Data
@Table(name = "role_")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
}
