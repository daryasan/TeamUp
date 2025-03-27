package org.example.models;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "queries_")
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Team team;
    private Long senderId;
    private Long receiverId;
    private QueryStatus queryStatus = QueryStatus.pinging;
}
