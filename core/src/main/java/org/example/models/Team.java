package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.UserDto;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "teams_")
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private String projectName;
    private Long mentorId;
    private Long leaderId;

   // @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Long> participants;

    @ManyToMany
    @JoinTable(
            name = "team_tags",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    private boolean formed;
    private boolean hasMentor;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meeting> meetings;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Query> queries;
}
