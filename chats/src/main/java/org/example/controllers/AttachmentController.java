package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.clients.FileClient;
import org.example.exceptions.MessageException;
import org.example.models.Attachment;
import org.example.services.AttachmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final FileClient fileClient;

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public Attachment uploadAttachment(
            @RequestParam("file") MultipartFile file) {
        String path = fileClient.uploadFile(file);
        return attachmentService.uploadAttachment(path);
    }

    @PatchMapping("/tie")
    public Attachment tieAttachmentToMessage(
            @RequestParam Long messageId,
            @RequestParam Long attachmentId
    ) throws MessageException {
        return attachmentService.tieAttachmentToMessage(messageId, attachmentId);
    }

    @GetMapping
    public byte[] downloadAttachment(
            @RequestParam String path
    ) {
        return fileClient.downloadFile(path);
    }


    @GetMapping("/id")
    public Attachment getAttachmentById(
            @RequestParam Long id
    ) throws MessageException {
        return attachmentService.getAttachmentById(id);
    }


    @GetMapping("/chats")
    public List<Attachment> getAttachmentsByChat(@RequestParam Long chatId) {
        return attachmentService.getAttachmentsByChat(chatId);
    }


}
