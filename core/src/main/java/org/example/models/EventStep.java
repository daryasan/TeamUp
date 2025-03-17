package org.example.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "event_steps_")
public class EventStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long eventId;
    private Date startDate;
    private Date endDate;
    private int stepNumber;
    private List<Long> winnersTeamsIds;
}
