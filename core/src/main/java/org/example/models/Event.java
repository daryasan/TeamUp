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
@Table(name = "events_")
public class Event {
    private long id;
    private String name;
    private String description;
    private String photoPath;
    private String webLink;
    private String prizeDescription;
  //  private List<Long> eventTagsId;
    private List<Long> participantsId;
    private List<EventStep> eventSteps;
    private List<Long> winnersId;
}
