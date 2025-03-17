package org.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventInfoDto {
    private String name;
    private String description;
    private String photoPath;
    private String webLink;
    private String prizeDescription;
    private Date startDate;
    private Date endDate;
}
