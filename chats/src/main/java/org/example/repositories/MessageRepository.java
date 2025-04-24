package org.example.repositories;

import org.example.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // get all chat messages sorted ascending
    List<Message> findByChatIdOrderByTimestampAsc(Long chatId);

    // paging
    List<Message> findByChatId(Long chatId, Pageable pageable);
}
