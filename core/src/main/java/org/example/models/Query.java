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
@Table(name = "queries_")
public class Query {
    private long id;
    private Team team;
    private Long userId;
    private QueryStatus queryStatus;
}
