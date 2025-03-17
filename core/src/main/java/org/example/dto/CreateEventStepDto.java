package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateEventStepDto {
    private Long eventId;
    private Date startDate;
    private Date endDate;
    private int stepNumber;
}
