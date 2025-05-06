package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.models.Attachment;
import org.example.services.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

//    @PostMapping
//    public ResponseEntity<AttachmentDto> uploadAttachment(
//            @RequestBody AttachmentDto attachmentDto) {
//        return null;
//    }


    @GetMapping("/{chatId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByChat(@PathVariable("chatId") Long chatId) {
        List<Attachment> attachments = attachmentService.getAttachmentsByChat(chatId);
        return ResponseEntity.ok(attachments);
    }


}
