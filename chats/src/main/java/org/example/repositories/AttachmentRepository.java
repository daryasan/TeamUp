package org.example.repositories;

import org.example.models.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    // get message attachments
    List<Attachment> findByMessageId(Long messageId);

}
