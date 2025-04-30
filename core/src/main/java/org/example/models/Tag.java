package org.example.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tags_")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long authorId;
    private String name;
}
