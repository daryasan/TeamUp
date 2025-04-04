package org.example.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "queries_")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private Event event;
    private String projectName;
    private Long mentorId;
    private Long leaderId;
    private List<Long> participantsId;
    private List<Tag> tags;
    private boolean formed;
    private boolean hasMentor;
    private List<Meeting> meetings;
    private List<Query> queries;
}
