package org.example.dto;

import lombok.Data;

@Data
public class EditEventDto {
    private String name;
    private String description;
    private String photoPath;
    private String webLink;
    private String prizeDescription;
}
