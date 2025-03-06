package org.example.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "queries_")
public class Team {
    private long id;
    private String name;
    private String description;
    private Event event;
    private String projectName;
    private Long mentorId;
    private Long leaderId;
    private List<Long> participantsId;
    // private List<Long> tagsId;
    private boolean statusIsFormed;
    private List<Meeting> meetings;
    private List<Query> queries;
}
