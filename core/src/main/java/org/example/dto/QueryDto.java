package org.example.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.example.models.QueryStatus;
import org.example.models.Team;

@Data
public class QueryDto {

    private Long id;
    private Long teamId;

    private Long senderId;
    private Long receiverId;

    private QueryStatus queryStatus = QueryStatus.pinging;
}
