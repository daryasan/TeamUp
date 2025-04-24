package org.example.repositories;

import org.example.models.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    // get chat participants
    List<ChatParticipant> findByChatId(Long chatId);

    // get all chats where user exists
    List<ChatParticipant> findByUserId(Long userId);
}
