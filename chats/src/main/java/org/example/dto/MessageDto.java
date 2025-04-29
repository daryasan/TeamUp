package org.example.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class MessageDto {
    Long chatId;
    Long senderId;
    String content;
}
