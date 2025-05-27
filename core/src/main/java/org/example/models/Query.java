package org.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;


@Data
@Entity
@Table(name = "queries_")
@NoArgsConstructor
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    private Long senderId;
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    private QueryStatus queryStatus = QueryStatus.pinging;
}
