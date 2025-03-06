package org.example.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
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
    private long id;
    private Event event;
    private Date startDate;
    private Date endDate;
    private int stepNumber;
    private List<Long> winners_id;
}
