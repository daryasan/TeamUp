package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.models.Attachment;
import org.example.repositories.AttachmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public List<Attachment> getAttachmentsByChat(Long chatId) {
        return attachmentRepository.findByMessageId(chatId);
    }
}
