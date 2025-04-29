package org.example.dto;

import lombok.Data;

@Data
public class AttachmentDto {
    Long chatId;
    Long senderId;
    String path;
    String fileType;
}
