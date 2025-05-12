package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.clients.FileClient;
import org.example.dto.AttachmentDto;
import org.example.exceptions.MessageException;
import org.example.models.Attachment;
import org.example.models.Message;
import org.example.repositories.AttachmentRepository;
import org.example.repositories.MessageRepository;
import org.hibernate.exception.DataException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.apigateway.model.Op;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;

    public List<Attachment> getAttachmentsByChat(Long chatId) {
        return attachmentRepository.findByMessageId(chatId);
    }

    @Transactional
    public Attachment uploadAttachment(String path) throws DataException {
        Attachment attachment = new Attachment();

        attachment.setFileUrl(path);

        attachmentRepository.save(attachment);

        return attachment;
    }

    public Attachment getAttachmentById(Long id) throws MessageException {
        Optional<Attachment> att = attachmentRepository.findById(id);
        if (att.isEmpty()) throw new MessageException("No such attachment!");
        return att.get();
    }


}
