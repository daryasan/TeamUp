package org.example.dto;

import lombok.Data;

@Data
public class TagDto {
    private Long id;
    private Long authorId = null;
    private String name;
}
