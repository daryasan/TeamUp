package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "event_steps_")
@NoArgsConstructor
@AllArgsConstructor
public class EventStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Long eventId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private int stepNumber;

    @OneToMany(mappedBy = "eventStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> winnerTeams;
}
