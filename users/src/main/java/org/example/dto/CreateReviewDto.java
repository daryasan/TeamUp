package org.example.dto;

import lombok.Data;

@Data
public class CreateReviewDto {
    private int rate;
    private String text;
}
