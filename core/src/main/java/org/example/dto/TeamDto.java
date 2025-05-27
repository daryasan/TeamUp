package org.example.dto;

import lombok.Data;
import org.example.models.Tag;

import java.util.List;

@Data
public class TeamDto {
    private long id;

    private String name;
    private String description;

    private Long eventId;

    private String projectName;
    private Long mentorId;
    private Long leaderId;

    private List<Long> participants;

    private List<Tag> tags;

    private boolean formed;
    private boolean hasMentor;
}
