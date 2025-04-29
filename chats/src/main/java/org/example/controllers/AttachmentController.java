package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.AttachmentDto;
import org.example.exceptions.ChatException;
import org.example.models.Attachment;
import org.example.services.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<AttachmentDto> uploadAttachment(
            @RequestBody AttachmentDto attachmentDto) {
        return null;
    }


    @GetMapping("/{chatId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByChat(@PathVariable("chatId") Long chatId) {
        List<Attachment> attachments = chatService.getAttachmentsByChat(chatId);
        return ResponseEntity.ok(attachments);
    }


}
