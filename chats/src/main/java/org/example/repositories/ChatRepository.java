package org.example.repositories;

import org.example.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
//
//    // get all chats where current user exists
//    List<Chat> findDistinctByParticipantsUserId(Long userId);
}
