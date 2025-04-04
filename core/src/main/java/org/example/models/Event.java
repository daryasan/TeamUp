package org.example.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "events_")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String photoPath;
    private String webLink;
    private String prizeDescription;
    private List<Tag> tags;
    private List<Long> participantsId;
    private List<EventStep> eventSteps;
    private List<Long> winnersId;
    private Date startDate;
    private Date endDate;
}
