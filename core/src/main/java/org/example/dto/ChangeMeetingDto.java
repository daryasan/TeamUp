package org.example.dto;

import lombok.Data;
import org.example.models.Team;

import java.util.Date;

@Data
public class ChangeMeetingDto {
    private String link;
    private Date startTime;
    private Date endTime;
}
