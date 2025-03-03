package org.example.models;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "role_")
public class Role {
    private long id;
    private String name;
}
