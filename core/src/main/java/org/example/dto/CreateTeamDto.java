package org.example.dto;

import lombok.Data;

@Data
public class CreateTeamDto {
    private String name;
    private Long eventId;
    private String projectName;
    private String description;
}
